package org.example.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserServiceApplication {

    public static void main(String[] args) {

//        System.out.println("User's Password : " + new BCryptPasswordEncoder().encode("user123"));
//        System.out.println("Admin's Password : " + new BCryptPasswordEncoder().encode("adminpass"));

        SpringApplication.run(UserServiceApplication.class, args);
    }

}


//----------------------------------------------------
//        -- USERS
//----------------------------------------------------
//
//INSERT INTO users(id, name, email, password, role) VALUES
//                                                       (1,'Admin One','admin1@gmail.com','$2a$10$LpQP.cZ6vaMRjbr1BHe5FOhgLlKGsi3E06tSK70IxnhmYkN8axmq2','ADMIN'),
//                                                               (2,'Admin Two','admin2@gmail.com','$2a$10$LpQP.cZ6vaMRjbr1BHe5FOhgLlKGsi3E06tSK70IxnhmYkN8axmq2','ADMIN'),
//                                                               (3,'Super Admin','admin3@gmail.com','$2a$10$LpQP.cZ6vaMRjbr1BHe5FOhgLlKGsi3E06tSK70IxnhmYkN8axmq2','ADMIN'),
//
//                                                               (4,'Harshal','harshal@gmail.com','$2a$10$g0XGmWEtotZufIvGzg33.udGPJCTynTGAnNrNa0xzp0bOOvWvnoH.','USER'),
//                                                               (5,'Rahul','rahul@gmail.com','$2a$10$g0XGmWEtotZufIvGzg33.udGPJCTynTGAnNrNa0xzp0bOOvWvnoH.','USER'),
//                                                               (6,'Sneha','sneha@gmail.com','$2a$10$g0XGmWEtotZufIvGzg33.udGPJCTynTGAnNrNa0xzp0bOOvWvnoH.','USER'),
//                                                               (7,'Priya','priya@gmail.com','$2a$10$g0XGmWEtotZufIvGzg33.udGPJCTynTGAnNrNa0xzp0bOOvWvnoH.','USER'),
//                                                               (8,'Amit','amit@gmail.com','$2a$10$g0XGmWEtotZufIvGzg33.udGPJCTynTGAnNrNa0xzp0bOOvWvnoH.','USER'),
//                                                               (9,'Rohan','rohan@gmail.com','$2a$10$g0XGmWEtotZufIvGzg33.udGPJCTynTGAnNrNa0xzp0bOOvWvnoH.','USER'),
//                                                               (10,'Pooja','pooja@gmail.com','$2a$10$g0XGmWEtotZufIvGzg33.udGPJCTynTGAnNrNa0xzp0bOOvWvnoH.','USER'),
//                                                               (11,'Kunal','kunal@gmail.com','$2a$10$g0XGmWEtotZufIvGzg33.udGPJCTynTGAnNrNa0xzp0bOOvWvnoH.','USER'),
//                                                               (12,'Neha','neha@gmail.com','$2a$10$g0XGmWEtotZufIvGzg33.udGPJCTynTGAnNrNa0xzp0bOOvWvnoH.','USER'),
//                                                               (13,'Vikas','vikas@gmail.com','$2a$10$g0XGmWEtotZufIvGzg33.udGPJCTynTGAnNrNa0xzp0bOOvWvnoH.','USER'),
//                                                               (14,'Anjali','anjali@gmail.com','$2a$10$g0XGmWEtotZufIvGzg33.udGPJCTynTGAnNrNa0xzp0bOOvWvnoH.','USER'),
//                                                               (15,'Saurabh','saurabh@gmail.com','$2a$10$g0XGmWEtotZufIvGzg33.udGPJCTynTGAnNrNa0xzp0bOOvWvnoH.','USER');
//
