# M365Downgrader

The files and documents in this repository are published under the terms of the [Creative Commons License](https://creativecommons.org/licenses/by-nc/4.0/).

In order to use the data of this repository, please indicate the source and give the corresponding credits.

**It is ABSOLUTELLY PROHIBITED the usage of this repository for commercial usage.**

The intent of this repository is to create a proxy program that allows you to intercept the miHome <-> xiaomi communication and will be able to modify it in order to inject a custom firmware to be sent to the M365 scooter.

The project requires the following tools:
- NodeJS in order to run proxy2.js server
- Java in order to decrypt and crypt the communication with xiaomi server
- Possibly (but not included in this repository) a fake DNS server. You can find a sample fake DNS server here: [FakeDNS Project](https://github.com/Crypt0s/FakeDns). An example of the dns.conf file for this software (```dns.conf```) is included in this repository.

This repository also includes the required files to dockerize the software. You can find it under the [Docker](docker/) folder.

You will also need to generate a set of certificates in order to establish a trust relationship between the app/your mobile and the server. The following commands generates some of them (you also need to download the CA keys under the [keys](keys/) folder. - ```JaimeCA.pem``` and ```JaimeCAKey.pem```):

```
openssl genrsa -out api.io.mi.com-Key.pem 2048
openssl req -new -key api.io.mi.com-Key.pem -out api.io.mi.com.csr
openssl x509 -req -in api.io.mi.com.csr -CA JaimeCA.pem -CAkey JaimeCAKey.pem -CAcreateserial -out api.io.mi.com.pem -days 10238 -sha256

openssl genrsa -out io.mi.com-Key.pem 2048
openssl req -new -key io.mi.com-Key.pem -out io.mi.com.csr
openssl x509 -req -in io.mi.com.csr -CA JaimeCA.pem -CAkey JaimeCAKey.pem -CAcreateserial -out io.mi.com.pem -days 10238 -sha256

openssl genrsa -out miKey.pem 2048
openssl req -new -key miKey.pem -out mi.csr
openssl x509 -req -in mi.csr -CA JaimeCA.pem -CAkey JaimeCAKey.pem -CAcreateserial -out mi.pem -days 10238 -sha256

openssl genrsa -out xiaomiKey.pem 2048
openssl req -new -key xiaomiKey.pem -out xiaomi.csr
openssl x509 -req -in xiaomi.csr -CA JaimeCA.pem -CAkey JaimeCAKey.pem -CAcreateserial -out xiaomi.pem -days 10238 -sha256
```

In addition to all of this, it is needed to generate a package to include the firmware. In order to do this, you need to create a version.json file like the following:
```
{"NormalVersion":
    {
        "CtrlVersionCode":["0200","26024"],
        "BleVersionCode":["0068","23948"],
        "BmsVersionCode":["0207","13100"]
    },
"TestVersion":
    {
        "CtrlVersionCode":["0200","26024"],
        "BleVersionCode":["0053","23840"],
        "BmsVersionCode":["0103","13071"]
    },
"TestDevice":
    [{"serial":"M1GCA1601C0001","id":"0","name":"haley"},
     {"serial":"M1GCA1601C0002","id":"0","name":"haley"},
     {"serial":"M1GCA1601C0003","id":"0","name":"haley"},
     {"serial":"M1GCA16062D8AC","id":"0","name":"haley"}]
}
```

Where ```CtrlVersionCode``` indicates the firmware version and size of the .bin file, ```BleVersionCode``` indicates the version of the Bluetooth communication module and ```BmsVersionCode``` indicates the BMS Version.
Once you have the .json file with the corresponding .bin files, it is needed to zip them and calculate the MD5 sum of the resulting package. That will be required by the application to verify the integrity of the package.
