using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;

using System.Web;
using System.Net;
using System.Net.Http;
using System.Net.Http.Headers;
using System.IO;
using System.Diagnostics;
// For more information on enabling MVC for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace Lauf.Controllers
{
    public class Results : Controller
    {
        public IActionResult Index(){
            WebRequest request = WebRequest.Create("https://testjson.search.windows.net/indexes/azuresql-index2/docs?api-version=2017-11-11&search=key&api-key=16099BB49F10E6A1183BB74CDEB481B9");
            WebResponse response = request.GetResponse();
            Console.WriteLine(((HttpWebResponse)response).StatusDescription);
            Stream dataStream = response.GetResponseStream();
            StreamReader reader = new StreamReader(dataStream);
            string responseFromServer = reader.ReadToEnd();
            Debug.WriteLine(responseFromServer);
            reader.Close();
            response.Close();
            return View();

        }
    }
}