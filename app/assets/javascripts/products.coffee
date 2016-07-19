jQuery ($) ->
  $table = $('.container table')
  productListUrl = $table.data('list')
  $.get productListUrl, (products) ->
    $.each products, (index, product) ->
      console.log(product)
      productInfo = product.name + " " + product.ean
      row = $('<tr/>').append $('<td/>').text(product.name) .append $('<td/>').text(product.ean)
      $table.append row