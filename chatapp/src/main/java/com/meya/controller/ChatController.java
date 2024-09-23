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
	
	// getMsg 메시지 얻어오기
	// 데이터가 생길 때마다 계속해서 보내준다. 
	@CrossOrigin // js 요청을 받을 수 있도록
	@GetMapping(value="/sender/{sender}/receiver/{receiver}", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Chat> getMsg(@PathVariable("sender") String sender , @PathVariable("receiver") String receiver) {
		return chatRepository.mFindBySender(sender, receiver).subscribeOn(Schedulers.boundedElastic());
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
