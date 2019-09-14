package foundation;


import java.lang.String;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

//斗地主案例
class Doudizhu {

    private static String[] pokerColours = {"♠","♥","♣","♦"};
    private static String[] pokerKing = {"大王","小王"};
    private static String[] pokerOrder = {"2","A","K","Q","J","10","9","8","7","6","5","4","3"};
    private static ArrayList<String> playerA = new ArrayList<>();
    private static ArrayList<String> playerB = new ArrayList<>();
    private static ArrayList<String> playerC = new ArrayList<>();
    private static ArrayList<String> hostCard = new ArrayList<>();

    void playGame(){
        ArrayList<String> assemblePoker;
        //1.组合牌
        try {
            assemblePoker = combinationPoker();
        }catch (Exception exception){
            System.out.println(exception.getMessage());
            return;
        }
        //2.洗牌
        shufflePoker(assemblePoker);

        //3.发牌
        dispatchPoker(assemblePoker);

        //3.5 整理牌
        sequencePoker(playerA);
        sequencePoker(playerB);
        sequencePoker(playerC);

        //4.看牌
        System.out.println("周润发："+playerA);
        System.out.println("刘德华："+playerB);
        System.out.println("周星驰："+playerC);
        System.out.println("底牌："+hostCard);

    }


    private ArrayList<String> combinationPoker() throws Exception{
        ArrayList<String> poker = new ArrayList<>(Arrays.asList(pokerKing));
        for (String color: pokerColours) {
            for (String order: pokerOrder) {
                poker.add(color+order);
            }
        }
        int pokerNumber = 54;
        if (poker.size()!= pokerNumber)
            throw new Exception("扑克牌组合失败！");
        return poker;
    }

    private <T> void shufflePoker(ArrayList<T> arrayList){
        Collections.shuffle(arrayList);
    }

    private void dispatchPoker(ArrayList<String> poker) {
        for (int i = 0; i < poker.size(); i++) {
            if (i>50) hostCard.add(poker.get(i));
            //取模分发牌
            if (i%3 ==0) playerA.add(poker.get(i));
            if (i%3 ==1) playerB.add(poker.get(i));
            if (i%3 ==2) playerC.add(poker.get(i));
        }
    }

    private void sequencePoker(ArrayList<String> arrayList){
        Collections.sort(arrayList);
    }
}
