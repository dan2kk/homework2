package homework2;

import java.util.ArrayList;
import java.util.Map;

public class Address {
    public static String[] confirmedDoName = {"경기", "경상남",
            "경상북",
            "전라남",
            "충청북",
            "충청남",
            "전라북",
            "경기",
            "강원",
            "제주"};
    public ArrayList<String> doroMung = new ArrayList<String>();
    public Address(String s){
        int count = 0;
        s = s.replaceAll("[^가-힣A-Za-z·\\d~.]", "");
        String[] temp = s.split("도|시|군|구");
        for(String doTemp: confirmedDoName){
            if(temp[0].contains(doTemp)){
                this.doroMung.add(doTemp + "도");
                int idx = s.indexOf(doTemp + "도");
                s = s.substring(idx+ doroMung.get(0).length());
                break;
            }
        }
        for(int i=0; i< temp.length-1; i++){
            //System.out.println("S:"+s);
            int idx = s.indexOf(temp[i]);
            if(idx == -1 || temp[i].isBlank())
                continue;
            this.doroMung.add(s.substring(0, idx+temp[i].length()+1));
            s = s.substring(idx+temp[i].length()+1);
        }
        /*
        System.out.print("Result: ");
        for(String t: doroMung){
            System.out.print(t+ " ");
        }
         */
    }

    public boolean addressMatch(Map<String, String> result){
        for(String w: doroMung){
            boolean flag = false;
            for(String s: result.values()){
                //System.out.println(w+" "+s);
                if(s!= null && w!=null && (s.contains(w)))
                    flag = true;
                else if(s!= null && w!=null && w.contains(s)){
                    doroMung.set(doroMung.indexOf(w), s);
                    flag = true;
                }
            }
            if(!flag)
                return false;
        }
        return true;
    }
    public String returnAddress(){
        String returnVal = "";
        for(String w: doroMung){
            returnVal += w + " ";
        }
        return returnVal;
    }
}
