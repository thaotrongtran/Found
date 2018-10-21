
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Lauf.Models;

namespace Lauf.Controllers
{
    public class HomeController : Controller
    {
        public IActionResult Index()
        {
            return View();
        }

        public IActionResult About()
        {
            ViewData["Message"] = "";

            return View();
        }
       
        public IActionResult Contact()
        {
            ViewData["Message"] = "\"We'd Love To Hear From You!\"";

            return View();
        }

        public IActionResult Privacy()
        {
            return View();
        }
        public IActionResult Return()
        {
            return RedirectToAction("Index","Results");
        }


        [ResponseCache(Duration = 0, Location = ResponseCacheLocation.None, NoStore = true)]
        public IActionResult Error()
        {
            return View(new ErrorViewModel { RequestId = Activity.Current?.Id ?? HttpContext.TraceIdentifier });
        }
    }
}
