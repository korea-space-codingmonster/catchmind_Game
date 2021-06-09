public class QuizDTO {
    private QuizDTO() {}

    private static class Holder {
        public static final QuizDTO INSTANCE = new QuizDTO();
    }

    public static QuizDTO getInstance() { return Holder.INSTANCE; }

    private int id;
    private String quiz;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuiz() {
        return quiz;
    }

    public void setQuiz(String quiz) {
        this.quiz = quiz;
    }
}