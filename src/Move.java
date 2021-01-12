import java.util.ArrayList;
import java.util.Arrays;

public class Move {

    String name;
    ArrayList moves;
    boolean blockedOnWay;
    boolean continued;
    boolean attacking;
    boolean move_only_with_attacking;
    char abbreviation;
    String move_type;

    public Move(char abbreviation, boolean blockedOnWay, boolean continued, boolean attacking, boolean move_only_with_attacking) {
        this.blockedOnWay = blockedOnWay;
        this.continued = continued;
        this.attacking = attacking;
        this.move_only_with_attacking = move_only_with_attacking;
        this.abbreviation = abbreviation;
        moves = new ArrayList();
        int[] pattern;
        switch (abbreviation) {

            case 'x':
                name = "diagonal";
                move_type = "standard";

                pattern = new int[]{1, 1};
                moves.add(pattern);
                pattern = new int[]{-1, 1};
                moves.add(pattern);
                pattern = new int[]{1, -1};
                moves.add(pattern);
                pattern = new int[]{-1, -1};
                moves.add(pattern);
                break;

            case '+':
                name = "straight";
                move_type = "standard";

                pattern = new int[]{-1, 0};
                moves.add(pattern);
                pattern = new int[]{1, 0};
                moves.add(pattern);
                pattern = new int[]{0, 1};
                moves.add(pattern);
                pattern = new int[]{0, -1};
                moves.add(pattern);
                break;

            case 'j':
                name = "knight jump";
                move_type = "standard";

                pattern = new int[]{2, 1};
                moves.add(pattern);
                pattern = new int[]{-2, 1};
                moves.add(pattern);
                pattern = new int[]{2, -1};
                moves.add(pattern);
                pattern = new int[]{-2, -1};
                moves.add(pattern);
                pattern = new int[]{1, 2};
                moves.add(pattern);
                pattern = new int[]{-1, 2};
                moves.add(pattern);
                pattern = new int[]{1, -2};
                moves.add(pattern);
                pattern = new int[]{-1, -2};
                moves.add(pattern);
                break;

            case 'c':
                name = "cat jump";
                move_type = "standard";

                pattern = new int[]{2, 2};
                moves.add(pattern);
                pattern = new int[]{-2, 2};
                moves.add(pattern);
                pattern = new int[]{2, -2};
                moves.add(pattern);
                pattern = new int[]{-2, -2};
                moves.add(pattern);
                break;

            case 'l':
                name = "leap";
                move_type = "standard";

                pattern = new int[]{-2, 0};
                moves.add(pattern);
                pattern = new int[]{2, 0};
                moves.add(pattern);
                pattern = new int[]{0, 2};
                moves.add(pattern);
                pattern = new int[]{0, -2};
                moves.add(pattern);
                break;

            case 'o':
                name = "king side castle";
                move_type = "conditional";

                break;

            case 'O':
                name = "queen side castle";
                move_type = "conditional";

                break;


            default:
                throw new IllegalStateException("Unexpected value: " + abbreviation);

        }
    }

        public Move(char abbreviation, int coulour, boolean attacking, boolean move_only_with_attacking){
            this.continued = false;
            this.attacking = attacking;
            this.move_only_with_attacking = move_only_with_attacking;
            this.abbreviation = abbreviation;

            moves = new ArrayList();
            int[] pattern;
            switch (abbreviation){
                case '1':
                    name = "one up";
                    move_type = "standard";

                    blockedOnWay = true;

                    if(coulour == 0){
                        pattern = new int[]{0, -1};
                        moves.add(pattern);
                    }
                    if(coulour == 1){
                        pattern = new int[]{0, 1};
                        moves.add(pattern);
                    }
                    break;

                case 'v':
                    name = "pawn fork";
                    move_type = "standard";

                    blockedOnWay = false;


                    if(coulour == 0){
                        pattern = new int[]{1, -1};
                        moves.add(pattern);
                        pattern = new int[]{-1, -1};
                        moves.add(pattern);
                    }
                    if(coulour == 1){
                        pattern = new int[]{1, 1};
                        moves.add(pattern);
                        pattern = new int[]{-1, 1};
                        moves.add(pattern);
                    }
                    break;

                case '2':
                    name = "rush";
                    move_type = "standard";

                    if(coulour == 0){
                        pattern = new int[]{0, -2};
                        moves.add(pattern);
                    }
                    if(coulour == 1){
                        pattern = new int[]{0, 2};
                        moves.add(pattern);
                    }
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + abbreviation);
            }
        }


}
