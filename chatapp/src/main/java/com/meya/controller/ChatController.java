package com.meya.controller;

import java.time.LocalDateTime;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.meya.Repository.ChatRepository;
import com.meya.document.Chat;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequiredArgsConstructor
public class ChatController {
	
	// 의존성 주입
	private final ChatRepository chatRepository;
	
	// 귓속말 같은거 할 때 사용하면 된다. 
	// getMsg 메시지 얻어오기
	// 데이터가 생길 때마다 계속해서 보내준다. 
	@CrossOrigin // js 요청을 받을 수 있도록
	@GetMapping(value="/sender/{sender}/receiver/{receiver}", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Chat> getMsg(@PathVariable("sender") String sender , @PathVariable("receiver") String receiver) {
		return chatRepository.mFindBySender(sender, receiver).subscribeOn(Schedulers.boundedElastic());
	}
	
	
	/*
	 * Spring WebFlux 를 이용하여 SSE(Server-Sent Events) 방식으로 실시간 채팅 데이터를 
	 * 스트리밍하는 API 엔드포인트
	 * 
	 * MediaType.TEXT_EVENT_STREAM_VALUE는 서버에서 클라이언트로 지속적으로 데이터를 스트리밍하기 위한 MIME 타입
	 * 이 엔드포인트가 텍스트 이벤트 스트림을 반환하는 SSE API 임을 나타낸다.
	 * 
	 * roomNum 에 해당하는 채팅 메시지를 조회한다.
	 * 
	 * subscribeOn : 리액티브 스트림이 어느 쓰레드에서 시작될지를 결정하는 메서드이다.
	 * Schedulers.boundedElastic()
	 * 
	 */
	// 우선 채팅 내역 모두 다 받아야한다.
	@CrossOrigin
	@GetMapping(value="/chat/roomNum/{roomNum}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Chat> findByUsername(@PathVariable("roomNum") Integer roomNum) {
		return chatRepository.mFindByRoomNum(roomNum)
				.subscribeOn(Schedulers.boundedElastic());
	}
	
	// 이제는 데이터 넣어주기
	// 데이터 한 건 리턴할꺼면 Mono, 여러건 할꺼면 Flux
	@CrossOrigin
	@PostMapping("/chat")
	public Mono<Chat> setMsg(@RequestBody Chat chat){
		chat.setCreatedAt(LocalDateTime.now());
		return chatRepository.save(chat);
	}
	

	
}
