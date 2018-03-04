package es.jaimemartin.utils;

/*
 * DEPENDS ON: java-json.jar
 */

public class Downgrader {

	static String serviceToken = "";
	static String ssecurity = "";
	static String nonce = "";
    
	public static void main(String[] args) {
		/* Receiving:
		 * args[0]: Action: c crypt, d decrypt
		 * args[1]: ssecurity (c y d)
		 * args[2]: nonce (c y d)
		 * args[3]: bms | firmware | all(only c) | cfw
		 * args[3]: body (only d) 
		 */
		
		
		/*
		 * URLs have to be in http:\\/\\/www.myhost.com\\/folder1\\/folder2\\/Package.zip\ form
		 * MD5 is the MD5 hash of Package.zip
		 */
		String respuestaAll =      "{\"code\":0,\"message\":\"ok\",\"result\":{\"version\":\"1.0.1_237\",\"url\":\"http:\\/\\/www.myhost.com\\/folder1\\/folder2\\/Package.zip\",\"changeLog\":\"\",\"md5\":\"MD5HASH\"}}";;
		String respuestaBMS =      "{\"code\":0,\"message\":\"ok\",\"result\":{\"version\":\"1.0.1_207\",\"url\":\"http:\\/\\/www.myhost.com\\/folder1\\/folder2\\/Package.zip\",\"changeLog\":\"\",\"md5\":\"MD5HASH\"}}";
		String respuestaFirmware = "{\"code\":0,\"message\":\"ok\",\"result\":{\"version\":\"1.0.1_200\",\"url\":\"http:\\/\\/www.myhost.com\\/folder1\\/folder2\\/Package.zip\",\"changeLog\":\"\",\"md5\":\"MD5HASH\"}}";
		String respuestaCFW =      "{\"code\":0,\"message\":\"ok\",\"result\":{\"version\":\"1.0.1_201\",\"url\":\"http:\\/\\/www.myhost.com\\/folder1\\/folder2\\/Package.zip\",\"changeLog\":\"\",\"md5\":\"MD5HASH\"}}";
		
		
		if(args.length > 2){
			ssecurity = args[1];
			nonce = args[2];
			Descifra descifra = new Descifra(serviceToken,ssecurity);
			if(args[0].equalsIgnoreCase("c")){
				try{
					String bodyTocado = respuestaAll;
					if(args.length > 3){
						if(args[3].equalsIgnoreCase("bms")){
							bodyTocado = respuestaBMS;
						}else if(args[3].equalsIgnoreCase("firmware")){
							bodyTocado = respuestaFirmware;
						}else if(args[3].equalsIgnoreCase("cfw")){
							bodyTocado = respuestaCFW;
						}
					}
					System.out.println(descifra.cifra(bodyTocado, nonce));
					//return 0;
				}catch(Exception ex){
					System.out.println("ERROR");
					//ex.printStackTrace();
					//return -1;
				}
			}else if(args[0].equalsIgnoreCase("d")){
				try{
					String descifrado = descifra.m22352a(args[3],nonce);
					if(descifrado.startsWith("{")){
						System.out.println(descifrado);
						//return 0;
					}else{
						System.out.println("ERROR");
						//return 1;
					}
				}catch(Exception ex){
					System.out.println("ERROR");
					//ex.printStackTrace();
					//return -1;
				}
			}else{
				System.out.println("Option '" + args[0] + "' not supported.");
				System.out.println("Incorrect parameters. Usage: Downgrader <c|d> <ssecurity> <nonce> [body]");
				//return 1;
			}
		}else{
			System.out.println("Incorrect parameters. Usage: Downgrader <c|d> <ssecurity> <nonce> [bms|firm|body]");
			System.out.println("body applies only for decypher operation.");
			System.out.println("If not specified a cypher package, firm is selected by default.");
			//return 1;
		}
	}
	

}
