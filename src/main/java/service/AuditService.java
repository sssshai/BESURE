package service;

public interface AuditService {
    boolean checkSig(String idP);

    boolean checkHash(String idP, byte[] txContent);
}
