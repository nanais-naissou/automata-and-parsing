package Recognizer;

public class RecComment extends aRecognizer implements iRecognizer {

    // CONSTRUCTOR
    public RecComment(iCharStream cs, iListener lr) {
        super(cs);
        this.listener = lr;
    }

    // LISTENER / NOTIFIER
    public interface iListener extends iRecognizer.iListener {
        void comment(String recognized);
    }

    private iListener listener;

    @Override
    public void notifyAccept(String recognized) {
        listener.comment(recognized);
    }

    @Override
    public void notifyError(String consumed) {
    }

    // STATES
    static final int SLASH = aAEF.REJECT + 1;             
    static final int IN_COMMENT = aAEF.REJECT + 2;       
    static final int STAR = aAEF.REJECT + 3;               
    static final int LINE_COMMENT = aAEF.REJECT + 4;       
    static final int STRING_IN_COMMENT = aAEF.REJECT + 5;
    static final int CHAR_IN_COMMENT = aAEF.REJECT + 6;  
    static final int ESCAPE_IN_STRING = aAEF.REJECT + 7;  
    static final int ESCAPE_IN_CHAR = aAEF.REJECT + 8;   

    // STEP
    public int step(char c) {
        consume(c);
        switch (state) {

            case aAEF.ENTRY:
                if (c == '/') return SLASH;
                else return aAEF.REJECT;

            case SLASH:
                if (c == '/') return LINE_COMMENT;   
                else if (c == '*') return IN_COMMENT; 
                else return aAEF.REJECT;
            case LINE_COMMENT:
                if (c == '\n') return aAEF.ACCEPT;
                else return LINE_COMMENT;

            case IN_COMMENT:
                if (c == '*') return STAR;
                else if (c == '"') return STRING_IN_COMMENT;
                else if (c == '`') return CHAR_IN_COMMENT;
                else return IN_COMMENT;

            case STAR:
                if (c == '/') return aAEF.ACCEPT; 
                else if (c == '*') return STAR; 
                else if (c == '"') return STRING_IN_COMMENT;
                else if (c == '`') return CHAR_IN_COMMENT;
                else return IN_COMMENT;
            case STRING_IN_COMMENT:
                if (c == '\\') return ESCAPE_IN_STRING;
                else if (c == '"') return IN_COMMENT;
                else return STRING_IN_COMMENT;

            case ESCAPE_IN_STRING:
                return STRING_IN_COMMENT;
            case CHAR_IN_COMMENT:
                if (c == '\\') return ESCAPE_IN_CHAR;
                else if (c == '`') return IN_COMMENT;
                else return CHAR_IN_COMMENT;

            case ESCAPE_IN_CHAR:
                return CHAR_IN_COMMENT;

            default:
                return aAEF.REJECT;
        }
    }
}
