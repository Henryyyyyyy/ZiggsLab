package me.henry.ziggs.udputils;

import java.net.InetAddress;

public interface UDPSocketCallback {
	int UDP_DATA = 1;

	/**
	 * 使用inetAddress时候必须要判断是否为null
	 * @param buffer
	 * @param inetAddress
	 * @param port
	 */
	void udp_receive(byte[] buffer, InetAddress inetAddress, int port);

	/**
	 * 使用inetAddress时候必须要判断是否为null
	 * @param buffer
	 * @param inetAddress
	 * @param port
	 */
	void udp_timeout(byte[] buffer, InetAddress inetAddress, int port);

}
