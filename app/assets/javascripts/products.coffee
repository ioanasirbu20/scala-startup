jQuery ($) ->
  $table = $('.container table')
  productListUrl = $table.data('list')


  loadProductTable = ->
    $.get productListUrl, (products) ->
      $.each products, (index, product) ->
        console.log(product)
        row = $('<tr/>').append $('<td/>').text(product.ean)
        row.attr 'contenteditable', true
        $table.append row
        loadProductDetails row

  productDetailsUrl = (eanCode) ->
    $table.data('details').replace '0', eanCode

  loadProductDetails = (tableRow) ->
      eanCode = tableRow.text()

      $.get productDetailsUrl(eanCode), (product) ->
        tableRow.append $('<td/>').text(product.name)
        tableRow.append $('<td/>')

  loadProductTable()

  saveRow = ($row) ->
    [ean, name] = $row.children().map -> $(this).text()
    product =
      ean: parseInt(ean)
      name: name
    jqxhr = $.ajax
      type: "PUT"
      url: productDetailsUrl(ean)
      contentType: "application/json"
      data: JSON.stringify product
    jqxhr.done (response) ->
      $label = $('<span/>').addClass('label label-succes')
      $row.children().last().append $label.text(response)
      $label.delay(3000).fadeOut()
    jqxhr.fail (data) ->
      $label = $('<span/>').addClass('label label-important')
      message = data.responseText || data.statusText
      $row.children().last().append $label.text(message)

  $table.on 'focusout', 'tr', () ->
    saveRow $(this)