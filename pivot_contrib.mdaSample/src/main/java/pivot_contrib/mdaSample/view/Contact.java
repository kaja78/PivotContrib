package pivot_contrib.mdaSample.view;

import org.apache.pivot.collections.List;

import pivot_contrib.mda.ViewModel;
import pivot_contrib.mdaSample.vo.ContactVo;

@ViewModel
public class Contact {
	protected ContactVo editedRecord;
	protected List<ContactVo> tableData;
}
