package jp.zenryoku.rpg.data.config;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@XmlRootElement( name="jobs")
@XmlAccessorType(XmlAccessType.FIELD)
public class Jobs {
    @XmlElement(name="job", type=Job.class)
    private List<Job> jobs;

    public Jobs() {
    }
}
