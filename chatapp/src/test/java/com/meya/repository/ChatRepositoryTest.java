package com.meya.repository;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.meya.Repository.ChatRepository;
import com.meya.document.Chat;

@SpringBootTest
public class ChatRepositoryTest {
	
	@Autowired
	private ChatRepository chatRepository;
	
	// 데이터가 저장되는지 테스트한다.
	@Test
	@DisplayName("저장 테스트")
	public void saveChatMessage() {
		Chat chat = new Chat();
		chat.setMsg("Love you, Haeun!");
		chat.setSender("God");
		chat.setReceiver("Haeun");
		chat.setCreatedAt(LocalDateTime.now());
				
		// 비동기 데이터 삽입
		chatRepository.save(chat).subscribe();
	}
	

	

}
