# FencingSchoolConnection

Client-server application for creating, modifying, deleting records of the administrator of the fencing school.

Stack: Java EE, Apache Tomcat, Servlet API, MySQL DB, JDBC, Java FX, Gson.

## Client part of the application

The client side of an application is a collection of graphical interface forms that interact with each other. From the forms, the API of the server application is addressed via an http connection, a request is sent to the server and a response is received from it in JSON format, then it is parsed and the results are displayed in the form of user interface elements. A set of forms and classes of the client side:

### 1. Authorization Form

The authorization form allows a registered user to log in to the system or go to the registration form.

![1](https://user-images.githubusercontent.com/79397536/121051530-34e5f280-c7c2-11eb-9cda-719aed85d298.PNG)

### 2. Registration Form

The registration form allows the user to register in the system or go to the authorization form.

![2](https://user-images.githubusercontent.com/79397536/121051536-357e8900-c7c2-11eb-99cc-c95b528e0a8f.PNG)

### 3. Main Form

The main form allows you to add a apprentice or trainer to the system, view information about a apprentice or trainer, make a logout or delete an authorized user.

![3](https://user-images.githubusercontent.com/79397536/121051540-36171f80-c7c2-11eb-9ac8-2c570e32d66f.PNG)

#### 3.1 Add Apprentice or Trainer Form

The form allows you to add a apprentice or trainer to the system.

![4](https://user-images.githubusercontent.com/79397536/121051543-36171f80-c7c2-11eb-9acb-0b28503aa619.PNG)

### 4. Trainer Info Form

The Trainer Info form allows you to edit the trainer's data, add or delete an element of the trainer's schedule, and delete a trainer.

![6](https://user-images.githubusercontent.com/79397536/121051548-36afb600-c7c2-11eb-82b1-9f18a3d86c8f.PNG)

#### 4.1 Add Trainer Schedule Item Form

The form allows you to add an element of the trainer's schedule.

![7](https://user-images.githubusercontent.com/79397536/121051550-37484c80-c7c2-11eb-840f-0de9a554f4ee.PNG)

### 5. Apprentice Info Form

The Apprentice Info form allows you to edit the apprentice's data, add or delete training for apprentice, and delete a apprentice.

![10](https://user-images.githubusercontent.com/79397536/121051558-37e0e300-c7c2-11eb-98c4-da7fe76c0535.PNG)

#### 5.1 Add Training Form

The Training Form allows you to add a training for the Apprentice.

![8](https://user-images.githubusercontent.com/79397536/121051553-37484c80-c7c2-11eb-81cb-2edd85b54dcd.PNG)

![9](https://user-images.githubusercontent.com/79397536/121051555-37484c80-c7c2-11eb-9dae-fc95856a20ca.PNG)

