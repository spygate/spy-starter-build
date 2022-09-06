package test.project.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spy.project.sequence.FastIdGenerator;
import spy.project.sequence.Sequence;
import spy.project.utils.ExcelUtils;
import test.project.testdata.ExcelData;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/excel")
public class ExcelController {

    @Autowired
    @Qualifier("dbId")
    private Sequence dbIdGenerator;

    @Autowired
    @Qualifier("fastId")
    private Sequence redisId;

    @GetMapping("/download")
    public void download(HttpServletResponse response) throws IOException {
        ExcelData ed = new ExcelData();
        List<ExcelData> data = ed.make(10);
        ExcelUtils.download(response, "测试", data);
    }

    @GetMapping("/seq")
    public String getSequence() {
        return dbIdGenerator.getNext("TES", "TK");
    }

    @GetMapping("/rseq")
    public String getReseq() {
        return redisId.getNext("TY", FastIdGenerator.BizKey.ORD);
    }

}
