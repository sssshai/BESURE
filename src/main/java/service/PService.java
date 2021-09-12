package service;

import it.unisa.dia.gas.jpbc.Element;
import pojo.VO.EHR;

public interface PService {
    /**
     * return pwP_star
     */
    Element blindPw(String pwP);

    /**
     * return spwP
     */
    Element getSpw(Element[] sigma_star, Element pwP_star, Element[] Qs, String pwP);

    void register_P(Element spwP, String[] idKS, String idCS, String idH, String idP, Element pwP_star);

    int authenticate(String idP);

    int sendAppointInfoToPatient(int idDLength, int tpDLength, int tkLength);

    /**
     * return pwP_star
     */
    EHR consult_P(String idP, String pwP);

    EHR sendCk_rou_y_rouToD(String idP, int stage, byte[] ck_rou_y_rou);
}
