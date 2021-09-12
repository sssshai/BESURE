package pojo.VO;

public class AuditResult {
    private boolean checkPn;
    private boolean checkSig;
    private boolean checkHash;

    public AuditResult() {
    }

    public AuditResult(boolean checkPn, boolean checkSig, boolean checkHash) {
        this.checkPn = checkPn;
        this.checkSig = checkSig;
        this.checkHash = checkHash;
    }

    @Override
    public String toString() {
        return "AuditResult{" +
                "checkPn=" + checkPn +
                ", checkSig=" + checkSig +
                ", checkHash=" + checkHash +
                '}';
    }

    public boolean isCheckPn() {
        return checkPn;
    }

    public void setCheckPn(boolean checkPn) {
        this.checkPn = checkPn;
    }

    public boolean isCheckSig() {
        return checkSig;
    }

    public void setCheckSig(boolean checkSig) {
        this.checkSig = checkSig;
    }

    public boolean isCheckHash() {
        return checkHash;
    }

    public void setCheckHash(boolean checkHash) {
        this.checkHash = checkHash;
    }
}
