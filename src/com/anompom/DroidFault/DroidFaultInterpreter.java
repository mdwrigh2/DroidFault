package com.anompom.DroidFault;

import android.util.Log;

public class DroidFaultInterpreter {
	private char[] input;
	private char[] mem = new char[100];
	private int mem_ptr = 0;
	private int input_ptr = 0;
	
	public DroidFaultInterpreter(String input){
		this.input = input.toCharArray();
	}
	
	public Character interpretUntilPrint() throws Exception{
		while(input_ptr < input.length){
			switch(input[input_ptr]){
				case '>':
					mem_ptr++;
					input_ptr++;
					break;
				case '<':
					mem_ptr--;
					input_ptr++;
					break;
				case '+':
					mem[mem_ptr] += 1;
					input_ptr++;
					break;
				case '-':
					mem[mem_ptr] -= 1;
					input_ptr++;
					break;
				case '.':
					input_ptr++;
					return mem[mem_ptr];
				case '[':
					if(mem[mem_ptr] == 0){
						while(input[input_ptr] != ']'){
							input_ptr++;
							if(input_ptr > input.length){
								throw new Exception("[ is missing matching ]");
							}
						}
					}else{
						input_ptr++;
					}
					break;
				case ']':
					if(mem[mem_ptr] != 0){
						while(input[input_ptr] != '['){
							input_ptr--;
							if(input_ptr < 0){
								throw new Exception("] is missing matching [");
							}
						}
					}
					else{
						input_ptr++;
					}
					break;					
			}
		}
		return null;
	}
	
	public String interpret() throws Exception{
		StringBuilder result = new StringBuilder();
		Character c = interpretUntilPrint();
		while(c != null){
			result.append(c);
			c = interpretUntilPrint();
		}
		return result.toString();
	}

}
