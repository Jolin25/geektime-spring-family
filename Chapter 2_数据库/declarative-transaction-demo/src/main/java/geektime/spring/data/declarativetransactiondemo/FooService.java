package geektime.spring.data.declarativetransactiondemo;

public interface FooService {
    void insertRecord();
    void insertThenRollback() throws RollbackException;
    void invokeInsertThenRollback() throws RollbackException;

    void requiredNested();

    void required();

    void requiredNew();
}
