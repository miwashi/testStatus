package com.miwashi;

import static org.junit.Assert.*;

import org.junit.Test;

import net.miwashi.receiver.Notification;

public class TestResultReport {

	private String json = "{" +
			"\"nodeName\":\"master\"," +
			"\"jobName\":\"escenic-barnkanalen--regressiontest\"," +
			"\"buildId\":\"2015-08-10_09-57-53\"," +
			"\"type\":\"2\"," +
			"\"jenkinsUrl\":\"https://barnjenkins.svti.svt.se​/\"," +
			"\"uuid\":\"88c0e044-9731-4c00-86dd-3526d2f687d0\"," +
			"\"version\":\"prod\"," +
			"\"buildNumber\":\"6760\"," +
			"\"platform\":\"linux\"," +
			"\"timeStamp\":\"1439193609139\"," +
			"\"gitCommit\":\"3cb8cfaf0b9ddd895b1ea7e79bbd3d65a2722ea1\"," +
			"\"size\":\"any\"," +
			"\"buildTag\":\"jenkins-escenic-barnkanalen--regressiontest-6760\"," +
			"\"grid\":\"http://svt-stoprod-seleniumgrid01:4444/wd/hub\"," +
			"\"browser\":\"firefox\"," +
			"\"name\":\"se.svt.test.nss.streams.OppetArkivVideoStreamTest.shouldStartFor[http://www.oppetarkiv.se/video/1689053]\"," +
			"\"host\":\"http://www.stage.svt.se\"," +
			"\"buildUrl\":\"https://barnjenkins.svti.svt.se​/job/escenic-barnkanalen--regressiontest/6760/\"," +
			"\"user\":\"jenkins\"," +
			"\"gitURL\":\"git@bitbucket.org:svtidevelopers/svtse-automated-browsertests.git\"," +
			"\"gitBranch\":\"origin/master\"," +
			"\"status\":\"SUCCESS\"" +
		"}";
	@Test
	public void test() {
		Notification result = new Notification(json);
		System.out.println("Group: " + result.getTestGroup());
		System.out.println("Subgroup: " + result.getTestSubGroup());
		System.out.println("Requirement: " +  result.getTestRequirement());
		System.out.println("Subject: " + result.getTestSubject());
		System.out.println("Subject: " + result.getTestSubjectKey());
	}

}
