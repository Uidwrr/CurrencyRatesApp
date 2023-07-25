public class CurrencyRatesApp {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Неверное количество аргументов");
            return;
        }

        String code = null;
        String date = null;

        for (String arg : args) {
            if (arg.startsWith("--code=")) {
                code = arg.substring(7);
            } else if (arg.startsWith("--date=")) {
                date = arg.substring(7);
            }
        }

        if (code == null || date == null) {
            System.out.println("Аргументы не заданны");
            return;
        }

        getCurrencyRate(code, date);
    };

    private static void getCurrencyRate(String code, String date) {
        private static void getCurrencyRate(String code, String date) {
            String url = "https://www.cbr.ru/scripts/XML_daily.asp?date_req=" + date;

            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpGet httpGet = new HttpGet(url);

            try {
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    String xml = EntityUtils.toString(entity);

                    JAXBContext jaxbContext = JAXBContext.newInstance(ValCurs.class);
                    Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

                    ValCurs valCurs = (ValCurs) unmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes()));

                    for (Valute valute : valCurs.getValute()) {
                        if (valute.getCode().equals(code)) {
                            System.out.println(valute.getName() + ": " + valute.getValue());
                            return;
                        }
                    }

                    System.out.println("Курс не найден");
                } catch (JAXBException e) {
                    System.out.println("Ошибка при парсинге XML");
                }
            }
        }
    }
