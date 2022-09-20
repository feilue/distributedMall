/*
 * Copyright (C) 2021  即时通讯网(52im.net) & Jack Jiang.
 * The MobileIMSDK v6.x Project. 
 * All rights reserved.
 * 
 * > Github地址：https://github.com/JackJiang2011/MobileIMSDK
 * > 文档地址：  http://www.52im.net/forum-89-1.html
 * > 技术社区：  http://www.52im.net/
 * > 技术交流群：320837163 (http://www.52im.net/topic-qqgroup.html)
 * > 作者公众号：“【即时通讯技术圈】”，欢迎关注！
 * > 联系作者：  http://www.52im.net/thread-2792-1-1.html
 *  
 * "即时通讯网(52im.net) - 即时通讯开发者社区!" 推荐开源工程。
 * 
 * BridgeProcessor.java at 2021-6-29 10:24:09, code by Jack Jiang.
 */
package net.feilue.IMCode.service.processor;

import net.feilue.IMCode.service.bridge.MQProvider;
import net.feilue.IMCode.service.network.MBObserver;
import net.feilue.IMCode.service.protocal.Protocal;
import net.feilue.IMCode.service.protocal.ProtocalFactory;
import net.feilue.IMCode.service.utils.LocalSendHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用作异构平台IM消息交互的桥接服务提供者。
 * 
 * @author Jack Jiang(http://www.52im.net/thread-2792-1-1.html)
 * @since 3.0
 */
public abstract class BridgeProcessor extends MQProvider
{
	private static Logger logger = LoggerFactory.getLogger(BridgeProcessor.class);  
	
	/** 收到消息的字符解码格式。默认utf-8。 */
	public final static String IMMQ_DECODE_CHARSET = "UTF-8";
	
	/** 消息队列服务器连接URI，形如：“amqp://admin:123456789@192.168.1.190” */
	public static String IMMQ_URI = "amqp://js:19844713@192.168.1.190";
	/** 消息中转队列：Web端IM转发至APP端的消息，本类是此队列的消费者，表示从Web端读取消息 */
	public static String IMMQ_QUEUE_WEB2APP = "q_web2app";
    /** 消息中转队列：APP端IM转发至Web端的消息，本类是此队列的生产者，表示将消息发送至Web端 */
	public static String IMMQ_QUEUE_APP2WEB = "q_app2web";
	
	public BridgeProcessor()
	{
		super(IMMQ_URI, IMMQ_QUEUE_APP2WEB, IMMQ_QUEUE_WEB2APP, "IMMQ", false);
	}
	
	/**
	 * 处理通过MQ中间件收到的桥接消息。
	 */
	@Override
	protected boolean work(byte[] contentBody)
	{
		try
		{
			String msg = new String(contentBody, IMMQ_DECODE_CHARSET);
			
			// just log for debug
			logger.info("[IMCORE-桥接↓] - [startWorker()中] 收到异构服务器的原始 msg："+msg+", 即时进行解析并桥接转发（给接收者）...");
			
			final Protocal p = ProtocalFactory.parse(msg, Protocal.class);
			p.setQoS(true);   // 开启QoS支持，确保消息被送达客户端
			p.setBridge(true);// 设置桥接发送标识（注意：此标识必须设置）
			
			// Netty的数据发送结果观察者：netty的数据发送结果是通过异步通知来实现的（这就
			// 是异步编程模型，跟Nodejs的promise、Androi里的RxJava、iOS的block道理一样）
			MBObserver sendResultObserver = new MBObserver(){
				@Override
				public void update(boolean sendOK, Object extraObj)
				{
					// 在线发送成功，通知回调
					if(sendOK)
					{
						realtimeC2CSuccessCallback(p);
						logger.info("[IMCORE-桥接↓] - "+p.getFrom()+"发给"+p.getTo()
								+"的指纹为"+p.getFp()+"的消息转发成功！【第一阶段APP+WEB跨机通信算法】");
						
					}
					// 在线没成功递出，那就只能离线处理罗
					else
					{
						logger.info("[IMCORE-桥接↓]>> 客户端"+p.getFrom()+"发送给"+p.getTo()+"的桥接数据尝试实时发送没有成功("
								+p.getTo()+"不在线)，将交给应用层进行离线存储哦... 【第一阶段APP+WEB跨机通信算法】");

						//*********************** 代码段20160914【3】：与【1】处是一样的，未重用代码的目的是简化代码逻辑
						// 提交回调，由上层应用进行离线处理
						boolean offlineProcessedOK = offlineC2CProcessCallback(p);

						// 离线处理成功（由于不同于普通的c2s消息，桥接的消息被离线
						// 成功后不需要发伪ACK给发送者（因为发送者处于异构的另一台服务器上））
						if(offlineProcessedOK)
						{
							logger.debug("[IMCORE-桥接↓]>> 向"+p.getFrom()+"发送"+p.getFp()
										+"的消息【离线处理】成功,from="+p.getTo()+". 【第一阶段APP+WEB跨机通信算法】");
						}
						else
						{
							logger.warn("[IMCORE-桥接↓]>> 客户端"+p.getFrom()+"发送给"+p.getTo()+"的桥接数据传输消息尝试实时发送没有成功，但上层应用层没有成" +
									"功(或者完全没有)进行离线存储，此消息将被服务端丢弃！ 【第一阶段APP+WEB跨机通信算法】");
						}
					}
				}
			};
			
			// 实时发送数据
			LocalSendHelper.sendData(p, sendResultObserver);
			
			return true;
		}
		catch (Exception e)
		{
			logger.warn("[IMCORE-桥接↓] - [startWorker()中] work()方法出错，本条错误消息被记录：" +
					""+e.getMessage(), e);
			return true;// 注意： 尽量不要返回false，不然消息被放回队列后容易死循环，出错作好记录备查即可！！
			//return false;
		}
	}
	
	/**
	 * 在线实时将消息桥接转发成功后被调用的回调方法。建议如果应用层需要作用户
	 * 的聊天消息记录，可以在本方法中处理。
	 * 
	 * @param p 本次桥接转发的消息包
	 */
	protected abstract void realtimeC2CSuccessCallback(Protocal p);
	
	/**
	 * 无法在线实时发送成功的消息将通过本回调方法进行处理。建议在此方法中作
	 * 离线处理，不然消息将被丢弃哦。
	 * 
	 * @param p 本次桥接转发的消息包
	 * @return true表示离线处理成功，false表示离线处理失败
	 */
	protected abstract boolean offlineC2CProcessCallback(Protocal p);
}
