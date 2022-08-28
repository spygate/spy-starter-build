package spy.project.objs;

import lombok.Data;
import java.util.List;

@Data
public class PageResponse<T> extends PageRequest {

    private Boolean hasMore;
    private Integer totalSize;
    private Integer totalPage;
    private List<T> content;

}
