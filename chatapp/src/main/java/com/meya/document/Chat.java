package com.meya.document;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection="chat")
public class Chat {

	@Id
	private String id;
	private String msg;
	private String sender; // 보내는 사람
	private String receiver; // 받는 사람
	private Integer roomNum; // 방 번호 - 같은 방에 있으면 같은 메시지들을 볼 수 있도록
	
	private LocalDateTime createdAt;
	
}
