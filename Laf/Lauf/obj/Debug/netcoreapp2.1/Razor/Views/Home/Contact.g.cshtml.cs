#pragma checksum "/Users/iLuna/Projects/Lauf/Lauf/Views/Home/Contact.cshtml" "{ff1816ec-aa5e-4d10-87f7-6f4963833460}" "be9a7614f23fc85fe18aee317f2751694f9428e1"
// <auto-generated/>
#pragma warning disable 1591
[assembly: global::Microsoft.AspNetCore.Razor.Hosting.RazorCompiledItemAttribute(typeof(AspNetCore.Views_Home_Contact), @"mvc.1.0.view", @"/Views/Home/Contact.cshtml")]
[assembly:global::Microsoft.AspNetCore.Mvc.Razor.Compilation.RazorViewAttribute(@"/Views/Home/Contact.cshtml", typeof(AspNetCore.Views_Home_Contact))]
namespace AspNetCore
{
    #line hidden
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Threading.Tasks;
    using Microsoft.AspNetCore.Mvc;
    using Microsoft.AspNetCore.Mvc.Rendering;
    using Microsoft.AspNetCore.Mvc.ViewFeatures;
#line 1 "/Users/iLuna/Projects/Lauf/Lauf/Views/_ViewImports.cshtml"
using Lauf;

#line default
#line hidden
#line 2 "/Users/iLuna/Projects/Lauf/Lauf/Views/_ViewImports.cshtml"
using Lauf.Models;

#line default
#line hidden
    [global::Microsoft.AspNetCore.Razor.Hosting.RazorSourceChecksumAttribute(@"SHA1", @"be9a7614f23fc85fe18aee317f2751694f9428e1", @"/Views/Home/Contact.cshtml")]
    [global::Microsoft.AspNetCore.Razor.Hosting.RazorSourceChecksumAttribute(@"SHA1", @"eb7d94722f089ec6741a2ff05b087a80e15a58f9", @"/Views/_ViewImports.cshtml")]
    public class Views_Home_Contact : global::Microsoft.AspNetCore.Mvc.Razor.RazorPage<dynamic>
    {
        #pragma warning disable 1998
        public async override global::System.Threading.Tasks.Task ExecuteAsync()
        {
#line 1 "/Users/iLuna/Projects/Lauf/Lauf/Views/Home/Contact.cshtml"
  
    ViewData["Title"] = "LaF Locations";

#line default
#line hidden
            BeginContext(49, 4, true);
            WriteLiteral("<h2>");
            EndContext();
            BeginContext(54, 17, false);
#line 4 "/Users/iLuna/Projects/Lauf/Lauf/Views/Home/Contact.cshtml"
Write(ViewData["Title"]);

#line default
#line hidden
            EndContext();
            BeginContext(71, 555, true);
            WriteLiteral(@"</h2>

<br />
<address>
    <b>Library</b><br />
    266 4th St NW, Atlanta, GA 30332<br />
    <abbr title=""Phone"">P:</abbr>
    678.404.7700
    <br />
    <br />
    <b>Clough Undergraduate Learning Commons</b><br />
    266 4th St NW, Atlanta, GA 30313<br />
    <abbr title=""Phone"">P:</abbr>
    678.404.7700
    <br />
    <br />
    <b>Klaus Advanced Computing Building</b><br />
    266 Ferst Dr NW, Atlanta, GA 30332<br />
    <abbr title=""Phone"">P:</abbr>
    404.385.6362
    <br />
</address>
<br />
<br />
<br />
<h3>");
            EndContext();
            BeginContext(627, 19, false);
#line 29 "/Users/iLuna/Projects/Lauf/Lauf/Views/Home/Contact.cshtml"
Write(ViewData["Message"]);

#line default
#line hidden
            EndContext();
            BeginContext(646, 241, true);
            WriteLiteral("</h3>\r\n<address>\r\n    <strong>Tech Support Team:</strong> <a href=\"mailto:Support@example.com\">Support@example.com</a><br />\r\n    <strong>Any Questions?:</strong> <a href=\"mailto:ContactUs@example.com\">ContactUs@example.com</a>\r\n</address>\r\n");
            EndContext();
        }
        #pragma warning restore 1998
        [global::Microsoft.AspNetCore.Mvc.Razor.Internal.RazorInjectAttribute]
        public global::Microsoft.AspNetCore.Mvc.ViewFeatures.IModelExpressionProvider ModelExpressionProvider { get; private set; }
        [global::Microsoft.AspNetCore.Mvc.Razor.Internal.RazorInjectAttribute]
        public global::Microsoft.AspNetCore.Mvc.IUrlHelper Url { get; private set; }
        [global::Microsoft.AspNetCore.Mvc.Razor.Internal.RazorInjectAttribute]
        public global::Microsoft.AspNetCore.Mvc.IViewComponentHelper Component { get; private set; }
        [global::Microsoft.AspNetCore.Mvc.Razor.Internal.RazorInjectAttribute]
        public global::Microsoft.AspNetCore.Mvc.Rendering.IJsonHelper Json { get; private set; }
        [global::Microsoft.AspNetCore.Mvc.Razor.Internal.RazorInjectAttribute]
        public global::Microsoft.AspNetCore.Mvc.Rendering.IHtmlHelper<dynamic> Html { get; private set; }
    }
}
#pragma warning restore 1591
