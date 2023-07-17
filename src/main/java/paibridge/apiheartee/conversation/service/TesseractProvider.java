package paibridge.apiheartee.conversation.service;

import net.sourceforge.tess4j.Tesseract;
import org.springframework.stereotype.Service;

@Service
public class TesseractProvider {

    public Tesseract getTesseract() {
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("src/main/resources/tessdata");
        tesseract.setLanguage("kor");

        return tesseract;
    }
}
