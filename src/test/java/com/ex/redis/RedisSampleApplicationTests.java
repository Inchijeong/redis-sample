package com.ex.redis;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;

@SpringBootTest
class RedisSampleApplicationTests {

	@Autowired
	StringRedisTemplate redisTemplate;
	
	@Test
	void contextLoads() {
	}

	@Test
	public void testStrings() {
		
		ValueOperations<String, String> vos = redisTemplate.opsForValue();
		String key = "string_test";

	    vos.set(key, "1"); // set: 값 삽입
	    String result1 = vos.get(key); // get: 값 가져오기
	    System.out.println("key: " + key + " / value: " + result1);

	    vos.increment(key); // increment: value 값을 1 증가 
	    String result2 = vos.get(key);
	    System.out.println("key: " + key + " / value: " + result2);
	    
	    vos.decrement(key); // decrement: value 값을 1 감소 
	    String result3 = vos.get(key);
	    System.out.println("key: " + key + " / value: " + result3);
	}
	
	@Test
	public void testList() {

	    ListOperations<String, String> los = redisTemplate.opsForList();
	    String key = "list_test";

	    los.remove(key, 0, key);
	    
//	    los.rightPush(key, "H");
	    los.rightPush(key, "e"); // rightPush: 오른쪽 부터 삽입
	    los.rightPush(key, "l");
	    los.rightPush(key, "l");
	    los.rightPush(key, "o");
	    los.leftPush(key, "H");  // leftPush: 왼쪽 부터 삽입
	    
	    los.rightPushAll(key, " ", "W", "o", "r", "l", "d"); // rightPushAll: 오른쪽에 모든 값 삽입

	    String character1 = los.index(key, 1); // index: 해당 인덱스에 있는 값 가져오기
	    System.out.println("character1: " + character1);

	    String characterLeft = los.index(key, 1); // leftPop: 왼쪽에 있는 값 가져오고 삭제
	    System.out.println("characterLeft: " + characterLeft);
	    
	    Long size = los.size(key); // size: List 크기
	    System.out.println("size: " + size);

	    List<String> resultRange = los.range(key, 0, 10); // index: 해당 범위에 있는 값 가져오기
	    System.out.println("ResultRange: " + Arrays.toString(resultRange.toArray()));
	}
	
	@Test
	public void testSet() {
		
	    SetOperations<String, String> sos = redisTemplate.opsForSet();
	    String key = "set_test";

	    sos.add(key, "H");
	    sos.add(key, "e");
	    sos.add(key, "l");
	    sos.add(key, "l");
	    sos.add(key, "o");

	    Set<String> members = sos.members(key); // members: Set 객체로 반환
	    System.out.println("members: " + Arrays.toString(members.toArray()));

	    Long size = sos.size(key); // size: Set 크기
	    System.out.println("size: " + size);

	    Cursor<String> cursor = sos.scan(key, ScanOptions.scanOptions().match("*").count(3).build());

	    while(cursor.hasNext()) {
	        System.out.println("cursor: " + cursor.next());
	    }
	}
	
	@Test
	public void testSortedSet() {

	    ZSetOperations<String, String> zsos = redisTemplate.opsForZSet();
	    String key = "test_zset";

	    zsos.add(key, "H", 1);
	    zsos.add(key, "e", 5);
	    zsos.add(key, "l", 10);
	    zsos.add(key, "l", 15);
	    zsos.add(key, "o", 20);

	    Set<String> range = zsos.range(key, 0, 5); // range: 해당 인덱스 범위 안에 값들 가져오기
	    System.out.println("range: " + Arrays.toString(range.toArray()));

	    Long size = zsos.size(key); // size: ZSet 크기
	    System.out.println("size: " + size);

	    Set<String> scoreRange = zsos.rangeByScore(key, 0, 13); // rangeByScore: 해당 스코어 범위 안에 값들 가져오기
	    System.out.println("scoreRange: " + Arrays.toString(scoreRange.toArray()));
	}
	
	@Test
	public void testHash() {
		
		HashOperations<String, Object, Object> hos = redisTemplate.opsForHash();
	    String key = "test_hash";

	    hos.put(key, "Hello", "111");
	    hos.put(key, "Hello2", "222");
	    hos.put(key, "Hello3", "333");

	    Object hello = hos.get(key, "Hello"); // 값 가져오기
	    System.out.println("hello = " + hello);

	    Long size = hos.size(key); // size: HashMap 크기
	    System.out.println("size = " + size);
	    
	    Map<Object, Object> entries = hos.entries(key); // entries: Map 객체로 반환
	    System.out.println("entries = " + entries.get("Hello2"));

	}
}
