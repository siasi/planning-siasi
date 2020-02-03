package model;

import static model.SideType.BEGIN;
import static model.SideType.END;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class ConstraintTest {

	private static final boolean VALID = true;
	private static final boolean NOT_VALID = false;

	String taskABegin;
	String taskAEnd;
	String taskBBegin;
	String taskBEnd;
	SideType constraintSideA;
	SideType constraintSideB;
	private boolean expectedToBeValid;

	public ConstraintTest(String taskABegin, String taskAEnd, String taskBBegin, String taskBEnd,
			SideType constraintSideA, SideType constraintSideB, boolean expectedToBeValid) {
		super();
		this.taskABegin = taskABegin;
		this.taskAEnd = taskAEnd;
		this.taskBBegin = taskBBegin;
		this.taskBEnd = taskBEnd;
		this.constraintSideA = constraintSideA;
		this.constraintSideB = constraintSideB;
		this.expectedToBeValid = expectedToBeValid;
	}

	@Parameterized.Parameters(name = "{index}: Test with FROM_BEGIN={0}, FROM_END={1}, TO_BEGIN={2}, TO_END={3}, FROM_SID={4}, TO_SIDE= {5}, result: 6}")
	public static Iterable<Object[]> data() {
		//@formatter:off
		return Arrays.asList(new Object[][] { 
			// ALL CASES WHERE THE CONSTRAINT IS SATISFIED
			{ "2020-01-01", "2020-01-07", "2020-01-08", "2020-01-10", BEGIN, BEGIN, VALID },
			{ "2020-01-01", "2020-01-07", "2020-01-05", "2020-01-10", BEGIN, END, VALID },
			{ "2020-01-01", "2020-01-07", "2020-01-08", "2020-01-10", END, BEGIN, VALID },
			{ "2020-01-01", "2020-01-07", "2020-01-05", "2020-01-10", END, END, VALID },
			// ALL CASES WHERE THE CONSTRAINT IS NOT SATISFIED
			{ "2020-01-03", "2020-01-07", "2020-01-01", "2020-01-10", BEGIN, BEGIN, NOT_VALID },
			{ "2020-01-10", "2020-01-12", "2020-01-05", "2020-01-09", BEGIN, END, NOT_VALID },
			{ "2020-01-01", "2020-01-07", "2020-01-06", "2020-01-10", END, BEGIN, NOT_VALID },
			{ "2020-01-01", "2020-01-07", "2020-01-05", "2020-01-06", END, END, NOT_VALID }
			 });
		//@formatter:on
	}

	@Test
	public void shouldValidateConstraintInAnyCase() {
		ConstraintSide from = new ConstraintSide(0, constraintSideA);
		ConstraintSide to = new ConstraintSide(1, constraintSideB);
		Constraint c = new Constraint(from, to);
		c.setFromTask(new Task("", LocalDate.parse(taskABegin), LocalDate.parse(taskAEnd)));
		c.setToTask(new Task("", LocalDate.parse(taskBBegin), LocalDate.parse(taskBEnd)));
		if (expectedToBeValid) {
			Assert.assertTrue(c.isValid());
		} else {
			Assert.assertFalse(c.isValid());
		}
	}

}
