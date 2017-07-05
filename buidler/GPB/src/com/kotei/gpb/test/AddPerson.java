package com.kotei.gpb.test;

// See README.txt for information and build instructions.

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import pb.gs.Game.AllotSeatNtf;

import com.example.tutorial.AddressBookProtos.AddressBook;
import com.example.tutorial.AddressBookProtos.Person;
import com.google.protobuf.InvalidProtocolBufferException;

/**
 * ���л�
 * @author zhenzhoul
 *
 */
class AddPerson {
  

  // Main function:  Reads the entire address book from a file,
  //   adds one person based on user input, then writes it back out to the same
  //   file.
  public static void main(String[] args) throws Exception {
	  /*
	  * �������л�����
	  */
	  AddressBook.Builder addressBook = AddressBook.newBuilder(); 
	  
	  /*
	  * ������Ϣʵ�����
	  */
	  Person.Builder person = Person.newBuilder();

	  //������������
	  person.setId(1);
	  person.setEmail("122206334@qq.com");
	  person.setName("������");
	  
	  
	  //�绰����
	  person.addPhone(
		      Person.PhoneNumber.newBuilder()
		        .setNumber("555-4321")
		        .setType(Person.PhoneType.HOME));


	  /*������Ϣ���*/
	  
    // Add an address.
    addressBook.addPerson(person);
    
 // 将数据写到输出流，如网络输出流，这里就用ByteArrayOutputStream来代替  
    ByteArrayOutputStream output = new ByteArrayOutputStream();  
    addressBook.build().writeTo(output);  
      
    // -------------- 分割线：上面是发送方，将数据序列化后发送 ---------------  
      
    byte[] byteArray = output.toByteArray();  
    
 // 接收到流并读取，如网络输入流，这里用ByteArrayInputStream来代替  
    ByteArrayInputStream input = new ByteArrayInputStream(byteArray);  
      
    // 反序列化  
    AddressBook  xxg2 = AddressBook.parseFrom(input);  
    
    
    
    
    AllotSeatNtf.Builder builder = AllotSeatNtf.newBuilder();
	builder.setSelfSeat(100);

	AllotSeatNtf allotSeatNtf = builder.build();
	 
	
	 // 将数据写到输出流，如网络输出流，这里就用ByteArrayOutputStream来代替  
	// 接收到流并读取，如网络输入流，这里用ByteArrayInputStream来代替  
   
    ByteArrayOutputStream outputs = new ByteArrayOutputStream();  
    allotSeatNtf.writeTo(outputs);  
    
   
	byte[] buf = output.toByteArray();

	try {
		 ByteArrayInputStream inputs = new ByteArrayInputStream(buf); 
		allotSeatNtf = AllotSeatNtf.parseFrom(inputs);
	} catch (InvalidProtocolBufferException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

    return;
    
//    // ���������л���д�뵽�ļ���
//    
//    File f = new File("d:/1.txt");
//    if(f.exists()){
//    	f.mkdir();
//    }
//    
//    FileOutputStream output = new FileOutputStream("d:/1.txt");
//    try {
//      addressBook.build().writeTo(output);
//    } finally {
//      output.close();
//    }
  }
}
