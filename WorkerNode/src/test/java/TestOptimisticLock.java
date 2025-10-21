import org.project.locks.OptimisticLock;
import org.project.storage.models.Document;

public class TestOptimisticLock {
    public static void main(String[] args) {
        OptimisticLock entity = new OptimisticLock(new Document("Test1"));

        System.out.println(entity.getDocument()); // Outputs: Document
        System.out.println(entity.getVersion()); // Outputs: 1

        int readVersion = entity.getVersion();

        entity.setDocument(new Document("Test"), readVersion);

        try {

            entity.setDocument(new Document("Test"), readVersion);
        } catch (Exception e) {
            System.err.println("error in optimistic lock");
        }
    }
}
