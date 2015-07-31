package cathywu.mailmonitor.monitor;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author lzwu
 * @since 7/28/15
 */
@Component
@EnableScheduling
public class Monitor {

    private static final Logger LOG = Logger.getLogger(Monitor.class);

    @Scheduled(fixedRate = 10000)
    public void receiveEmails() {

    }
}
