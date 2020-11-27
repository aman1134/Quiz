package com.java.prj.quiz;

import java.util.List;

public class ContainerClass {
    static class Question{
        String question, answer;
        List<String> options;

        public Question() {
        }

        public Question(String question, String answer, List<String> options) {
            this.question = question;
            this.answer = answer;
            this.options = options;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public List<String> getOptions() {
            return options;
        }

        public void setOptions(List<String> options) {
            this.options = options;
        }
    }

    static class Quiz{
        String qid, topic, creator, creatorid;
        List<Question> qList;
        int  mcq_no, sec;



        public  Quiz(){}

        public Quiz(String qid, String topic, String creator, String creatorid, List<Question> qList, int mcq_no, int sec) {
            this.qid = qid;
            this.topic = topic;
            this.creator = creator;
            this.creatorid = creatorid;
            this.qList = qList;
            this.sec = sec;
            this.mcq_no = mcq_no;
        }

        public int getSec() {
            return sec;
        }

        public void setSec(int sec) {
            this.sec = sec;
        }

        public int getMcq_no() {
            return mcq_no;
        }

        public void setMcq_no(int mcq_no) {
            this.mcq_no = mcq_no;
        }

        public String getQid() {
            return qid;
        }

        public void setQid(String qid) {
            this.qid = qid;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public String getCreator() {
            return creator;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }

        public String getCreatorid() {
            return creatorid;
        }

        public void setCreatorid(String creatorid) {
            this.creatorid = creatorid;
        }

        public List<Question> getqList() {
            return qList;
        }

        public void setqList(List<Question> qList) {
            this.qList = qList;
        }
    }

    static class User{
        String name, cno, uid;

        public User(){}

        public User(String name, String cno, String uid) {
            this.name = name;
            this.cno = cno;
            this.uid = uid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCno() {
            return cno;
        }

        public void setCno(String cno) {
            this.cno = cno;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }
    }
}
