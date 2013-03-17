(function($, Edge, compId){
   //Edge symbol: 'stage'
   (function(symbolName) {
	   Edge.Symbol.bindTriggerAction(compId, symbolName, "Default Timeline", 24125, function(sym, e) {
         // play the timeline from the given position (ms or label)
         sym.play(0);
      });
      //Edge binding end
   })("stage");
   //Edge symbol end:'stage'
})(jQuery, AdobeEdge, "sitePiggyPen");