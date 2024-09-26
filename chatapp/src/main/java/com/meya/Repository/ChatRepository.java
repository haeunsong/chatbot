package com.meya.Repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;

import com.meya.document.Chat;

import reactor.core.publisher.Flux;

/*
 * ReactiveMongoRepository
 * : Spring Data MongoDB 에서 제공하는 리포지토리
 * MongoDB 에 대한 리액티브(비동기) 데이터 접근을 지원한다.
 */
public interface ChatRepository extends ReactiveMongoRepository<Chat, String> {

	/*
	 * @Query : 커스텀 MongoDB 쿼리를 정의하는데 사용한다.
	 * BSON 형식 쿼리를 작성하여 sender 와 receiver 필드를 기준으로 데이터를 조회한다. 
	 * 
	 * Flux<Chat> : 여러개의 Chat 데이터를 스트리밍 방식으로 반환한다. Flux 는 비동기적으로 연속적인 데이터의 흐름을 표현한다.
	 * 이 경우 하나의 결과값이 아닌 여러 개의 Chat 문서를 계속해서 반환할 수 있다.
	 * 
	 * 특정 sender와 receiver가 일치하는 모든 Chat 데이터를 스트리밍 방식으로 반환하며, 결과가 나오는 즉시 클라이언트로 전달됩니다.
	 * 데이터를 한 번에 모두 반환하는게 아니라, 데이터가 준비되는 대로 스트리밍하여 순차적으로 전달한다. 
	 * 비동기식 환경에서 고성능 시스템을 구축하는데 유용하다.
	 * 
	 * mydb> db.chat.find({sender:'God',receiver:'Haeun'});
	 * 
	 * @Tailable
	 * : 고정크기 컬렉션(capped collection) 에서 실시간 데이터 스트리밍을 가능하게 해주는 기능이다.
	 * 이를 통해 몽고db 컬렉션에서 실시간으로 변경된 데이터를 계속해서 조회할 수 있다.
	 * Capped collection은 고정된 크기를 가지는 컬렉션으로, 데이터가 꽉 차면 가장 오래된 데이터부터 덮어쓰는 구조입니다. 
	 * 일반 컬렉션과 달리 순차적으로 데이터를 저장하고, 데이터 삭제나 인덱스 작업이 불가능합니다.
	 */
	@Tailable // 커서를 안닫고 계속 유지한다.
	@Query("{sender: ?0, receiver: ?1}")
	Flux<Chat> mFindBySender(String sender, String receiver); // Flux (흐름) response 를 유지하면서 데이터를 계속 흘려보내기
	
	@Tailable
	@Query("{roomNum:?0}")
	Flux<Chat> mFindByRoomNum(Integer roomNum);
	
}
