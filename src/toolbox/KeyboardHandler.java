package toolbox;

import org.lwjgl.input.Keyboard;


public class KeyboardHandler {
    
    int turn;
    
    public KeyboardHandler(){
        turn = 0;
    }
    
    public KeyboardHandler(int turn){
        this.turn = turn;
    }
    
    
    public int getTurn(){
        return checkInputs();
    }
    
    private int checkInputs() {      
        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                if (Keyboard.getEventKey() == Keyboard.KEY_RETURN) {
                    turn = (turn+1)%2;		
                    System.out.println("turn: " + turn);
                }
            }
            /*else {    
                if (Keyboard.getEventKey() == Keyboard.KEY_RETURN) {
                    System.out.println("A Key Released");
                }
            }*/
            
        }
        return turn;
    }
}
