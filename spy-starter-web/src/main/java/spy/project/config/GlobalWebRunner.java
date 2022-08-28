package spy.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import spy.project.web.WebEnvGetterPrinter;

public class GlobalWebRunner implements ApplicationRunner {

    @Autowired
    private WebEnvGetterPrinter webEnvGetterPrinter;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        webEnvGetterPrinter.print();
    }
}
