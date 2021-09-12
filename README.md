# BESURE: Blockchain-Based Cloud-Assisted eHealth Systems with Secure Data Provenance

## Environment required

- JDK 11+ (Using **IntelliJ IDEA** to compile and deploy this **Maven Web** projects is recommended)
- MySQL 8.0+
- Truffle v4.1.10 and Solidity v0.4.24 (the path of truffle had been added into system environment variables)
- Ganache v2.5.4 (a mock blockchain system)
- Tomcat 8.5.40

## Deploy the smart contract

1. Open Ganache client
2. Compile the source code  
   `mvn compile`
3. Configure Tomcat server  
   set HTTP port as 8888 and URL as http://localhost:8888/BESURE/registration
4. Run Tomcat server  
   Ganache generates two transactions and smart contract has been deployed successfully.

## Create database

Use the MySQL management tools to run the SQL script files **in the sql file folder** to generate five tables, among
which the parameter table `t_params` has initial data, and other tables only have structure.

## Login in the system

The system has one user role: creator.

After entering the system index page, you can upload, edit, transfer, delete, download the file.

Except for the download operation, every operation on the file will generate a transaction record on the **ganache**
blockchain, and the file table `t_file` and traceability record table `t_provenance` of the database will add
corresponding records.

## _Note_

- After shutting down the Tomcat server, `t_ehr` `t_user` `t_ks` `t_h` `t_cs` tables will be clear to for next test.
