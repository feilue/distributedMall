package net.feilue.IMCode.service.bridge;///*
// * Copyright (C) 2020  即时通讯网(52im.net) & Jack Jiang.
// * The MobileIMSDK v5.x Project. 
// * All rights reserved.
// * 
// * > Github地址：https://github.com/JackJiang2011/MobileIMSDK
// * > 文档地址：  http://www.52im.net/forum-89-1.html
// * > 技术社区：  http://www.52im.net/
// * > 技术交流群：320837163 (http://www.52im.net/topic-qqgroup.html)
// * > 作者公众号：“【即时通讯技术圈】”，欢迎关注！
// * > 联系作者：  http://www.52im.net/thread-2792-1-1.html
// *  
// * "即时通讯网(52im.net) - 即时通讯开发者社区!" 推荐开源工程。
// * 
// * QoS4ReciveDaemonC2B.java at 2020-8-22 16:00:59, code by Jack Jiang.
// */
//package net.feilue.IMCode.service.bridge;
//
//import net.feilue.IMCode.service.qos.QoS4ReciveDaemonRoot;
//
///**
// * 异构服务器消息转发模式下QoS机制之对已收到包进行有限生命周期存储并提供
// * 重复性判断的守护线程实现类。
// * <p>
// * "C2B" means "client to bridge".
// * 
// * @author Jack Jiang(http://www.52im.net/thread-2792-1-1.html)
// * @version 1.0
// * @since 3.0
// */
//public class QoS4ReciveDaemonC2B extends QoS4ReciveDaemonRoot
//{
//	private static QoS4ReciveDaemonC2B instance = null;
//	
//	public static QoS4ReciveDaemonC2B getInstance()
//	{
//		if(instance == null)
//			instance = new QoS4ReciveDaemonC2B();
//		return instance;
//	}
//	
//	public QoS4ReciveDaemonC2B()
//	{
//		// ** 关于参数值的说明
//		//    为了降低服务端负载，【1】【2】参数的设定要定不能太大，太大会导致服务
//		//    端处理桥接消息的QoS算法时内存消耗增大、处理效率降低，太小则达不到QoS
//		//    算法的目的，实际部署时一定要根据普遍的网络延迟及服务器性能、处理效率上找到最优值
//		//    > 参数【1】的值越大，QoS接收队列的处理越及时，但如果队列很大的话，处理耗时必然很长
//		//      ，频繁调用则肯定会加到服务端负载。建议间隔以客户端重传一个包的最大间隔这准。
//		//    > 参数【2】的值越大，QoS接收队列会越长，近而导致QoS机制的一系列效率问题，建
//		//      议不要超过客户端的最多重传时间总和。
//		
//		super(5 * 1000  //【1】检查线程执行间隔（单位：毫秒），本参数<=0表示使用父类的默认值
//			, 15 * 1000 //【2】一个消息放到在列表中（用于判定重复时使用）的生存时长（单位：毫秒），本参数<=0表示使用父类的默认值
//			, true
//			, "-桥接QoS！");
//	}
//}