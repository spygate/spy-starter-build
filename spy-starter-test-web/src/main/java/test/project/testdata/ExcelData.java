package test.project.testdata;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
public class ExcelData {

    @ExcelProperty("用户名")
    private String username;
    @ExcelProperty("用户年龄")
    private Integer userage;
    @ExcelProperty("用户余额")
    private BigDecimal useramount;
    @ExcelProperty("用户操作日期")
    private String usertime;

    public List<ExcelData> make(int n) {
        List<ExcelData> data = new ArrayList();
        for(int i = 0; i < n; i++) {
            ExcelData ed = new ExcelData();
            ed.setUsername("xxxx");
            ed.setUserage(11);
            ed.setUseramount(new BigDecimal("21.22"));
            ed.setUsertime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            data.add(ed);
        }
        return data;
    }

}
