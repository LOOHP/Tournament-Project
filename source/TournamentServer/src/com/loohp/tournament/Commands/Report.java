package com.loohp.tournament.Commands;

import java.util.Optional;

import com.loohp.tournament.Utils.ReportGenerator;

public class Report {
	
	public static void generate(String[] args) {
		
		if (args.length == 1) {
			ReportGenerator.output(Optional.empty());
		} else {
			ReportGenerator.output(Optional.of(args[1]));
		}
	}

}
