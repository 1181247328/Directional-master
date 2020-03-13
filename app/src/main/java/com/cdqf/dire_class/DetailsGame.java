package com.cdqf.dire_class;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DetailsGame {
    private int id;
    private String title;
    private String img;
    private List<Answer> answer = new CopyOnWriteArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public List<Answer> getAnswer() {
        return answer;
    }

    public void setAnswer(List<Answer> answer) {
        this.answer = answer;
    }

    public class Answer {
        private int id;
        private String title;
        private String img;
        private String topic;
        private String ble;
        private String correct;
        private String one;
        private String two;
        private String three;
        private String four;
        private boolean isSelete = false;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public String getBle() {
            return ble;
        }

        public void setBle(String ble) {
            this.ble = ble;
        }

        public String getCorrect() {
            return correct;
        }

        public void setCorrect(String correct) {
            this.correct = correct;
        }

        public String getOne() {
            return one;
        }

        public void setOne(String one) {
            this.one = one;
        }

        public String getTwo() {
            return two;
        }

        public void setTwo(String two) {
            this.two = two;
        }

        public String getThree() {
            return three;
        }

        public void setThree(String three) {
            this.three = three;
        }

        public String getFour() {
            return four;
        }

        public void setFour(String four) {
            this.four = four;
        }

        public boolean isSelete() {
            return isSelete;
        }

        public void setSelete(boolean selete) {
            isSelete = selete;
        }
    }
}
