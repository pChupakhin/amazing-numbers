package numbers;

import java.util.Scanner;

public class Main{

	public static void main(String[] args) {
		final Scanner scanner = new Scanner(System.in);
		Request request = new Request(Request.INSTRUCTIONS_PRINT_REQUEST);
		Response response = new Response(request);
		System.out.print("\nWelcome to Amazing Numbers!\n" + response);

		while(!request.forProgramTerminate()) {
			request = new Request(scanner.nextLine());
			response = new Response(request);
			System.out.print(response);
		}
	}

}
