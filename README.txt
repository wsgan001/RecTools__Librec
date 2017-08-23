################ Dependencies ############
java 1.8



################ Steps for ranking models #############
1. find the java file \librec\core\src\main\java\net\librec\run\Params.java, change rootDir
2. change log dirrectory, librec.log.dir in \librec\core\src\main\java\net\librec\run\Params.java logFilePath file
3. change data setting, data path in \librec\core\src\main\java\net\librec\run\Params.java baseConfigPath file
4. run \librec\core\src\main\java\net\librec\run\generate_configs_ranking.java to generate model configs
5. run \librec\core\src\main\java\net\librec\run\test_all_ranking_pure.java, all results are in \librec\core\src\main\java\net\librec\run\log
