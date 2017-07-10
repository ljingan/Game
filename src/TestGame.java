import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import com.common.handler.UserHandler;
import com.common.net.TcpChannel;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pb.gs.Game.AllotSeatNtf;

public class TestGame {
	public static void main(String[] args) throws Exception {
		// // 服务端在20006端口监听客户端请求的TCP连接
		// ServerSocket server = new ServerSocket(20006);
		// Socket client = null;
		// System.out.println("start\n");
		// boolean f = true;
		// while (f) {
		// // 等待客户端的连接，如果没有获取连接
		// client = server.accept();
		// System.out.println("与客户端连接成功！");
		// // 为每个客户端连接开启一个线程
		// new Thread(new ServerThread(client)).start();
		// }
		// server.close();
		TcpChannel server = new TcpChannel();
		Thread thread = new Thread(server);
		thread.start();
		System.out.print("启动\n");
		new UserHandler();

		AllotSeatNtf.Builder builder = AllotSeatNtf.newBuilder();
		builder.setSelfSeat(100);

		AllotSeatNtf allotSeatNtf = builder.build();
		 
		
		 // 将数据写到输出流，如网络输出流，这里就用ByteArrayOutputStream来代替  
		// 接收到流并读取，如网络输入流，这里用ByteArrayInputStream来代替  
       
        ByteArrayOutputStream output = new ByteArrayOutputStream();  
        allotSeatNtf.writeTo(output);  
        
       
		byte[] buf = output.toByteArray();

		try {
			 ByteArrayInputStream input = new ByteArrayInputStream(buf); 
			allotSeatNtf = AllotSeatNtf.parseFrom(input);
		} catch (InvalidProtocolBufferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
