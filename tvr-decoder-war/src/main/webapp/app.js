function doDecode() {
  $("#display").slideUp('slow', function () {
    $("#display").html("Loading...").show();
    var tag = $('#tag_field').val();
    var meta = $('#tagmetaset_field').val();
    var value = $('#value_field').val();
    if (ga) {
      ga('send', 'event', tag, meta);
    }
    if (localStorage && window.location.href.indexOf("localhost") != -1) {
      localStorage.setItem("tag", tag);
      localStorage.setItem("tagmetaset", meta);
      localStorage.setItem("value", value);
    }
    $.post('/t/decode', {tag: tag, value: value, meta: meta}, displayDecoding);
  });
}

function getPermalinkUrl() {
  var tag = $('#tag_field').val();
  var meta = $('#tagmetaset_field').val();
  var value = $('#value_field').val();
  var linkBack = '/t/decode/' + tag + '/' + meta + '/' + encodeURIComponent(value);
  return window.location.href.replace(window.location.pathname, linkBack);
}

function displayDecoding(data) {
  $("#display").html(data).slideDown('slow', function () {
    $(".decoded,.composite-decoded").each(function () {
      $(this).click(function (e) {
        highlightBytes($(this));
        e.stopPropagation();
      });
    });
    $(".expander").each(function () {
      $(this).click(function (e) {
        toggleExpander($(this));
        e.stopPropagation();
      });
      initExpander($(this));
    });
    allPopovers().popover({
      html: true,
      trigger: 'hover',
      position: 'right',
      content: function () {
        var short = this.getAttribute("data-short");
        var long = this.getAttribute("data-long");
        return '<div class="short-background">' + short + '</div>' + (long ? '<div class="long-background">' + long + '</div>' : '');
      },
      delay: {show: 0, hide: 400}
    });
    if (allPopovers().length > 0) {
      $("#popoverChoice").show();
    } else {
      $("#popoverChoice").hide();
    }
    $('#link-back').show().attr("href", getPermalinkUrl());
  });
}

function onOptionChange() {
  var value_field = $('#value_field');
  var tagField = document.getElementById('tag_field');
  var maxLength = tagField.options[tagField.selectedIndex].getAttribute("data-maxlength");
  value_field.attr("maxLength", maxLength);
  if (value_field.val().length > maxLength) {
    value_field.val(value_field.val().substr(0, maxLength))
  }
}

var highlighted = null;

function highlightBytes(decoded) {
  clearHighlight();
  decoded.addClass("highlight");
  highlighted = decoded;

  var rawDataId = decoded.attr("data-i");
  var start = +decoded.attr("data-s");
  var end = +decoded.attr("data-e");
  $("#rawData-" + rawDataId).show("fast");
  var i = 0;
  for (i = start; i < end; i++) {
    $("#b-" + i).addClass("highlight");
  }
}

function clearHighlight() {
  if (highlighted != null) {
    highlighted.removeClass("highlight");
  }
  highlighted = null;
  $(".bytes").removeClass("highlight");
}

function hideRawData(rawDataId) {
  clearHighlight();
  $("#rawData-" + rawDataId).hide("fast");
}

function getExpander(e) {
  var itemId = e[0].getAttribute("data-item");
  var collapsed = e.hasClass("glyphicon-zoom-in");
  var detail = $('.detail[data-item="' + itemId + '"]');
  var chunk = $('.composite-chunked[data-item="' + itemId + '"]');
  return {collapsed: collapsed, detail: detail, chunk: chunk};
}

function initExpander(e) {
  var expander = getExpander(e);
  if (expander.collapsed) {
    expander.detail.addClass("collapsed");
    expander.chunk.addClass("expanded");
  } else {
    expander.detail.addClass("expanded");
    expander.chunk.addClass("collapsed");
  }
}

function toggleExpander(e) {
  var expander = getExpander(e);
  if (expander.collapsed) {
    expander.detail.addClass("expanded").removeClass("collapsed");
    expander.chunk.removeClass("expanded").addClass("collapsed");
    e.removeClass("glyphicon-zoom-in").addClass("glyphicon-zoom-out")
  } else {
    expander.detail.removeClass("expanded").addClass("collapsed");
    expander.chunk.addClass("expanded").removeClass("collapsed");
    e.removeClass("glyphicon-zoom-out").addClass("glyphicon-zoom-in")
  }
}

function allPopovers() {
  return $('.apdu-label[data-short]:not([data-short=""])');
}
function yupWeAreLoaded() {
  function loadLastValue(key) {
    var last = localStorage.getItem(key);
    if (last) {
      $('#' + key + '_field').val(last);
    }
    return !!last;
  }

  if (localStorage && window.location.href.indexOf("localhost") != -1 && $('#value_field').val() == '') {
    var haveLastSet = ['tag', 'tagmetaset', 'value'].reduce(function(haveAll, key) {
      return loadLastValue(key) && haveAll;
    }, true);
    if (haveLastSet) {
      console.log('dev mode load');
      doDecode();
    }
  }
  onOptionChange();

  $("body").on("shown.bs.popover", function(ev) {
    allPopovers().each(function() {
      if (ev.target != this) {
        $(this).popover('hide');
      }
    })
  });

  $('#link-back').popover({
    html: true,
    trigger: 'hover',
    placement: 'bottom',
    content: function () {
      var url = getPermalinkUrl();
      return '<input type="text" id="permalink" value="' + url +'" onclick="this.focus();this.select()"></input>';
    },
    delay: {show: 0, hide: 400}
  });
}

function disablePopovers(input) {
  var state = input.checked ? 'enable' : 'disable';
  allPopovers().popover(state);
}

(function() {
  var originalLeave = $.fn.popover.Constructor.prototype.leave;
  $.fn.popover.Constructor.prototype.leave = function(obj){
    var self = obj instanceof this.constructor ?
      obj : $(obj.currentTarget)[this.type](this.getDelegateOptions()).data('bs.' + this.type);
    var container, timeout;

    originalLeave.call(this, obj);

    if(obj.currentTarget) {
      container = $(obj.currentTarget).siblings('.popover');
      timeout = self.timeout;
      container.one('mouseenter', function(){
        clearTimeout(timeout);
        container.one('mouseleave', function(){
          $.fn.popover.Constructor.prototype.leave.call(self, self);
        });
      })
    }
  };
})();