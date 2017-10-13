package me.henry.ziggslab.tcp;


import static android.content.ContentValues.TAG;

public interface TCPSocketCallback {
	/**
	 * 
	 * 断开连接
	 */
	int TCP_DISCONNECTED = 0;
	/**
	 * 
	 * 已连接
	 */
	int TCP_CONNECTED = 1;
	/**
	 * 
	 * 连接获得数据
	 */
	int TCP_DATA = 2;

	/**
	 * 
	 * 当建立连接是的回调
	 */
	void tcp_connected(String ip);

	/**
	 * 
	 * 当获取网络数据回调接口
	 * 
	 * 
	 * 
	 * @param buffer
	 * 
	 *            字节数据
	 */
	void tcp_receive(byte[] buffer);

	/**
	 * 
	 * 当断开连接的回调
	 */
	void tcp_disconnect(String ip);

//	private TCPSocketConnect getGateConnect(GateModel model) {
//		TCPSocketConnect tcp = new TCPSocketConnect(new TCPSocketCallback() {
//			@Override
//			public void tcp_connected(String ip) {
//				Message msg = new Message();
//				msg.obj = ip;
//				msg.what = TCP_SUCCESS;
//				if (gHandler != null)
//					gHandler.sendMessage(msg);
//			}
//
//			@Override
//			public void tcp_receive(byte[] buffer) {
//				Message msg = new Message();
//				msg.obj = buffer;
//				msg.what = TCP_MSG;
//				if (gHandler != null)
//					gHandler.sendMessage(msg);
//			}
//
//			@Override
//			public void tcp_disconnect(String ip) {
//				Message msg = new Message();
//				msg.obj = ip;
//				msg.what = TCP_FAIL;
//				if (gHandler != null)
//					gHandler.sendMessage(msg);
//			}
//		});
//
//		tcp.setAddress(model.ip, model.port);
//
//		return tcp;
//	}



//	private void createConnect() {
//		isCreateConnect = true;
//		snList = GateModel.getGateControlList(gateModelList);
//		for (int i = 0; i < snList.size(); i++) { // 建立多个TCP连接
//			tcpLists.add(getGateConnect(snList.get(i)));
//			new Thread(tcpLists.get(i)).start();
//		}
//		XLog.d(TAG,"createConnect="+isCreateConnect);
//	}
}
