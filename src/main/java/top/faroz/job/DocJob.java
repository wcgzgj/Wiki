package top.faroz.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName TestJob
 * @Description TODO
 * @Author FARO_Z
 * @Date 2021/4/10 下午2:18
 * @Version 1.0
 **/
@Component
public class DocJob {
    private static final Logger LOG = LoggerFactory.getLogger(DocJob.class);


    /**
     * 自定义cron表达式跑批
     * 只有等上一次执行完成，下一次才会在下一个时间点执行，错过就错过
     * cron 表达式不用记，可以在这个网址生成：https://cron.qqe2.com/
     *
     * 下面这段表示：每隔 30 秒，更新一次电子书信息
     */
    @Scheduled(cron = "1/30 * * * * ? ")
    public void cron() throws InterruptedException {
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss SSS");
        String dateString = formatter.format(new Date());
    }
}
