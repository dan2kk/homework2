# 주소 파싱 과제

## 과제 명세

# 과제

고객이 수기로 작성하여 올바른 주소인지 보장할 수 없는 대량의 주소 목록이 있습니다.

건 별로 주소를 검사하여, 주소가 어느 "로"("길")인지 확인하려고 합니다.

최대한 많은 주소를 올바른 주소인지 확인할 수 있도록 프로그램을 작성해주세요.

프로그램은 실행이 가능해야 하며, 실행 방법을 작성해주세요.

언어 : java, 버전 무관

라이브러리, 오픈소스 등 사용 자유

프로그램 입력/출력은 아래와 같이 해주시면 됩니다.

- 입력 형식 : 문자열
- 출력 형식 : 자유

(예)

입력 : 성남, 분당 백 현 로 265, 푸른마을 아파트로 보내주세요!!

출력 : 백현로

입력 : 마포구 도화-2길 코끼리분식

출력 : 마포구 도화2길

## 과제 구성

1. [https://business.juso.go.kr/addrlink/attrbDBDwld/attrbDBDwldList.do?cPath=99MD&menu=주소DB](https://business.juso.go.kr/addrlink/attrbDBDwld/attrbDBDwldList.do?cPath=99MD&menu=%EC%A3%BC%EC%86%8CDB)  주소DB를 Excel을 통해 csv로 가공
2. 이를 SQLite에 올려 데이터 베이스 구축, 도로명주소 + 광역자치단체 + 기초자치단체 ⇒ Primary Key
3. 가장 뒤에 있는 (’길’ 과 ‘로’)의 index를 검색하여 0번째 글자부터 index-1번째 글자까지 db 탐색, 정확히 일치하는 도로명이 있는지 확인
4. 만약 결과값이 단 한개 있다면 해당 결과값을 반환한다.
5. 만약 결과값이 여러 개있다면 도로명 앞 쪽에 있는 주소를 한 글자씩 역순으로 광역자치단체 혹은 기초자치단체에 포함되어 있는지 확인한다.
6. 만약 그래도 여러 개라면 그중 첫 번째 것을 반환한다.
7. 만약 null이 발생하면 옳바르지 않은 주소로 판단한다. ex)서울 백 현 로

### 시간 복잡도

백현로 + 길로 * 27개를 통해 최악의 케이스 생각

⇒ 27* 26 / 2 ⇒ 351번의 문자열에 대해서 도로명 탐색

즉 시간 복잡도는 O(N^2)이다. 이중 for문은 지양해야 하므로 다른 방법이 있을지 생각해보자.

이중 For문으로 DB요청…? 내재화된 DB라 속도에 크게 문제가 있지는 않을 수도 있다. 하지만 정적인 데이터인데 DB를 사용할 이유가 있을까…?

그렇다면 HashMap을 통해 직렬화하고, 대신 크기는 10MB정도 

Object를 담아놓고 Key값으로 도로명주소? ⇒ 같은 도로명 주소지만 다른 단체일때는?

다른 자료형태는 단순 List랑 다를 게 없다!

차라리 DB를 만들고 Index를 도로명주소에 대해서 만들어 검색 성능을 향상시킨다.

![스크린샷 2023-07-21 오후 12.13.57.png](%E1%84%8C%E1%85%AE%E1%84%89%E1%85%A9%20%E1%84%91%E1%85%A1%E1%84%89%E1%85%B5%E1%86%BC%20%E1%84%80%E1%85%AA%E1%84%8C%E1%85%A6%20dd5f8ca40b52496c8432cf0ad10b4e3b/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-07-21_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_12.13.57.png)

### IO Example

1. 성남, 분당 백 현 로 265, 푸른마을 아파트로 보내주세요!!
2. 마포구 도화-2길 코끼리분식 
3. 부산광역시 apec로에 보내주세요!!
4. 합포 3·15대로 버거킹
5. 집은 강남구 삼성로 212 은마아파트입니다. 
6. **제주시 516로**
7. 서울 구로구 구로중앙로 구로아파트
8. 삼척시 중앙로
9. 평양시 중앙로(존재하지 않는 주소)
10. 서울 백현로(존재하지 않는 주소)

1. 백현로

1. 마포구 도화2길
2. 부산광역시 APEC로
3. 3·15대로
4. 강남구 삼성로
5. 옳바르지 않는 주소입니다
6. 옳바르지 않는 주소입니다
7. 삼척시 중앙로
8. 옳바르지 않는 주소입니다
9. 옳바르지 않는 주소입니다

![스크린샷 2023-07-20 오후 2.09.38.png](%E1%84%8C%E1%85%AE%E1%84%89%E1%85%A9%20%E1%84%91%E1%85%A1%E1%84%89%E1%85%B5%E1%86%BC%20%E1%84%80%E1%85%AA%E1%84%8C%E1%85%A6%20dd5f8ca40b52496c8432cf0ad10b4e3b/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-07-20_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_2.09.38.png)

`내로새똠길 ⇒ 내로새c길`로 데이터 오류가 발생해 있었다는 사실 발견

### 중점적으로 생각해야 할 부분(Feedback)

1. 데이터 저장방식 ⇒ 데이터베이스 구조를 어떻게 구성할 것인가?
2. 문자열 파싱 조직 ⇒ 어떻게 효율적으로 처리할 것인가?
3. Testing 방법 ⇒ 테스트 자동화를 통해 어떻게 효율적인 개발을 할 것인가?

## Version 2

> GitHub 링크: [https://github.com/dan2kk/homework2/tree/main](https://github.com/dan2kk/homework2/tree/main)
> 

### 데이터베이스 구조

![Untitled](%E1%84%8C%E1%85%AE%E1%84%89%E1%85%A9%20%E1%84%91%E1%85%A1%E1%84%89%E1%85%B5%E1%86%BC%20%E1%84%80%E1%85%AA%E1%84%8C%E1%85%A6%20dd5f8ca40b52496c8432cf0ad10b4e3b/Untitled.png)

“도, 시, 군, 구”에 대한 테이블을 (index, name)값으로 구성하고, 상위 계층이 존재하는 경우 upperclass에 외래키 참조를 통해 상위 계층에 대한 정보를 읽어 올 수 있도록 relation 구성

도로명주소 테이블은 도로명주소 전체에 대한 정보를 삽입하여 (도로명, 도, 시, 군, 구)에 대한 정보를 저장

ex) 

![스크린샷 2023-07-26 오후 1.56.29.png](%E1%84%8C%E1%85%AE%E1%84%89%E1%85%A9%20%E1%84%91%E1%85%A1%E1%84%89%E1%85%B5%E1%86%BC%20%E1%84%80%E1%85%AA%E1%84%8C%E1%85%A6%20dd5f8ca40b52496c8432cf0ad10b4e3b/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-07-26_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_1.56.29.png)

### 데이터 가공

- 파이썬을 통해 기존에 가공했었던 `homework2.csv` 를 재 가공하여 이를 db에 담는 형식을 사용
- 각각 단위에서 중복 제거를 위해 리스트에 담을 때, `lst[i].count(column)` 을 통해 기존에 존재하는 column인지 확인 후 저장
- `lst[i].index(column)` 을 통해 해당 행정구역에 대해 위치를 탐색하여 이를 도로명주소 table에 저장하는 방식

```python
 #python3

import sqlite3
con = sqlite3.connect("Address_db.db")
cur = con.cursor()

import csv
count = [0 for _ in range(5)]
lst = [[] for _ in range(5)]
reader = csv.reader(open('homework2.csv', 'r'), delimiter=',')
for row in reader:
    formalindex = [None for _ in range(4)]
    for column in row:
        #print(column)
        if len(column) == 0:
            continue
        elif column[-1] == "도":
            if lst[0].count(column) == 0:
                lst[0].append((column))
            formalindex[0] = lst[0].index(column)
        elif column[-1] == "시":
            if lst[1].count((column, formalindex[0])) == 0:
                lst[1].append((column, formalindex[0]))
            formalindex[1] = lst[1].index((column, formalindex[0]))
        elif column[-1] == "군":
            if lst[2].count((column, formalindex[0])) == 0:
                lst[2].append((column, formalindex[0]))
            formalindex[2] = lst[2].index((column, formalindex[0]))
        elif column[-1] == "구":
            if lst[3].count((column, formalindex[1])) == 0:
                lst[3].append((column, formalindex[1]))
            formalindex[3] = lst[3].index((column, formalindex[1]))
        elif column[-1] == "로" or column[-1] == "길":
            lst[4].append((column, formalindex))
        else:
            continue
for idx, x in enumerate(lst[0]):
    cur.execute("INSERT INTO 도 VALUES (?, ?)", (idx, x))
for idx, x in enumerate(lst[1]):
    cur.execute("INSERT INTO 시 VALUES (?, ?, ?)", (idx, x[0], x[1]))
for idx, x in enumerate(lst[2]):
    cur.execute("INSERT INTO 군 VALUES (?, ?, ?)", (idx, x[0], x[1]))
for idx, x in enumerate(lst[3]):
    cur.execute("INSERT INTO 구 VALUES (?, ?, ?)", (idx, x[0], x[1]))
for idx, x in enumerate(lst[4]):
    cur.execute("INSERT INTO 도로명주소 VALUES (?, ?, ?, ?, ?, ?)", (idx, x[0], x[1][0], x[1][1], x[1][2], x[1][3]))

con.commit()
```

![Untitled](%E1%84%8C%E1%85%AE%E1%84%89%E1%85%A9%20%E1%84%91%E1%85%A1%E1%84%89%E1%85%B5%E1%86%BC%20%E1%84%80%E1%85%AA%E1%84%8C%E1%85%A6%20dd5f8ca40b52496c8432cf0ad10b4e3b/Untitled%201.png)

![스크린샷 2023-07-24 오후 4.48.34.png](%E1%84%8C%E1%85%AE%E1%84%89%E1%85%A9%20%E1%84%91%E1%85%A1%E1%84%89%E1%85%B5%E1%86%BC%20%E1%84%80%E1%85%AA%E1%84%8C%E1%85%A6%20dd5f8ca40b52496c8432cf0ad10b4e3b/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-07-24_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_4.48.34.png)

### 문자열 파싱 로직

![Untitled](%E1%84%8C%E1%85%AE%E1%84%89%E1%85%A9%20%E1%84%91%E1%85%A1%E1%84%89%E1%85%B5%E1%86%BC%20%E1%84%80%E1%85%AA%E1%84%8C%E1%85%A6%20dd5f8ca40b52496c8432cf0ad10b4e3b/Untitled%202.png)

### 문자열 파싱 참고 코드(ChatGPT)

```java
import java.util.*;
import java.util.regex.*;

public class AddressParser {

    public static void main(String[] args) {
        // 예시 도로명주소
        String address = "서울특별시 강남구 테헤란로 123";
        
        // 도로명주소를 구성 요소로 나누기
        Map<String, String> parsedAddress = parseAddress(address);
        System.out.println(parsedAddress);
    }

    public static Map<String, String> parseAddress(String address) {
        // 정규표현식 패턴 설정
        String pattern = "([^\\s]+(?:도|특별시|광역시))\\s+([^\\s]+(?:시|군|구))\\s+(.*)";
        
        // 패턴 컴파일과 매칭
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(address);
        
        // 매칭 결과를 담을 맵 객체 생성
        Map<String, String> parsedAddress = new HashMap<>();
        
        if (matcher.find()) {
            parsedAddress.put("도", matcher.group(1));
            parsedAddress.put("시군구", matcher.group(2));
            parsedAddress.put("나머지주소", matcher.group(3));
        } else {
            System.out.println("주소 형식이 잘못되었습니다.");
        }
        
        return parsedAddress;
    }
}
```