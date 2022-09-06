package test.project.web.controller;

//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spy.project.auth.JwtUser;
import spy.project.exceptions.FrameException;
import spy.project.exceptions.codes.ErrorCode;
import spy.project.log.LogIgnore;
import spy.project.utils.ParallelTemplete;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//@Api("其他测试类")
@RestController
@RequestMapping("/other")
public class OtherController {

    /**
     * 并发执行工具使用示例
     */
//    @ApiOperation("并发调度测试")
    @GetMapping("/thread")
    public void threadWork() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        ParallelTemplete templete = new ParallelTemplete(executorService, 3);

        templete.addWorks(new ParallelTemplete.TaskTemplete() {
            @Override
            public Object dowork() {
                return "1 work";
            }
        }, String.class);

        templete.addWorks(new ParallelTemplete.TaskTemplete() {
            @Override
            public Object dowork() {
                return "2 work";
            }
        }, String.class);

        templete.addWorks(new ParallelTemplete.TaskTemplete() {
            @Override
            public Object dowork() {
                return "3 work";
            }
        }, String.class);

        List<ParallelTemplete.FT> res = templete.startWorks();

        res.forEach(v -> {
            try {
                if(v.getResult() instanceof String i) {
                    System.out.println(i);
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

    }

    @LogIgnore
//    @ApiOperation("获取用户登陆信息测试")
    @GetMapping("/loginuser")
    public JwtUser loginUser(JwtUser jwtUser) {
        return jwtUser;
    }

//    @ApiOperation("异常测试")
    @GetMapping("/error")
    public void throwError() {
        throw new FrameException(ErrorCode.ERR0003);
    }


}
