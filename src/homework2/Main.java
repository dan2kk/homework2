package homework2;

import java.util.*;

public class Main {
    public static ArrayList<Integer> findIdx(String addr){ //로, 길에 해당하는 index 찾기, 가장 뒤에 있는 글자 부터 찾기시작
        ArrayList<Integer> result = new ArrayList<>();
        while(addr.length() != 0){
            int roLastIdx = addr.lastIndexOf("로");
            int gilLastIdx = addr.lastIndexOf("길");
            if(roLastIdx > gilLastIdx) { //로가 뒤에 있으면 roLastIdx를 추가하고 로 앞까지 문자열 자르기
                result.add(roLastIdx);
                addr = addr.substring(0, roLastIdx);
            }
            else if(roLastIdx < gilLastIdx){ //길이 뒤에 있으면 gilLastIdx를 추가하고 길 앞까지 문자열 자르기
                result.add(gilLastIdx);
                addr = addr.substring(0, gilLastIdx);
            }
            else{ //만약 길과 로가 같이 있다면 문자열에 더이상 길, 로가 존재하지 않음
                break;
            }
        }
        return result;
    }
    public static void run(SqlConn sqlconn, String input){
        Address newAddress = new Address(input);
        input = input.replaceAll("[^가-힣A-Za-z·\\d~.]", "");
        input = input.toUpperCase(); //"APEC로" 검색을 위한 대문자화
        ArrayList<Integer> idxList = findIdx(input); //로, 길에 대한 index 탐색
        String result;
        for(int i = 0; i!= idxList.size(); i++){
            for(int j=0; j<idxList.get(i); j++){
                //System.out.println(input.substring(j, idxList.get(i)+1));
                List<Map<String, String>> resultList = sqlconn.selectAddress(input.substring(j, idxList.get(i)+1));
                if(!resultList.isEmpty()){
                    //System.out.println(resultList);
                    result = resultList.get(0).get("도로명");
                    for(Map<String, String> r: resultList){
                        r.remove("도로명");
                        if(newAddress.addressMatch(r)) {
                            result = newAddress.returnAddress() + result;
                            System.out.println(result);
                            return;
                        }
                    }
                }
            }
        }
        System.out.println("옳바르지 않은 주소입니다");
    }
    public static void main(String[] args){
        Scanner io = new Scanner(System.in);
        SqlConn sqlconn = new SqlConn("jdbc:sqlite:Address_db.db");
        System.out.print("주소입력: (q 종료)");
        String input;
        while((input= io.nextLine()).equals("q")){
            run(sqlconn, input);
        }
        sqlconn.closeDbConn();
    }
}
