$(document).ready(function() {
	$('#myInst').multiselect({
		includeSelectAllOption: true,
		enableCaseInsensitiveFiltering: true,
		checkboxName: 'myInst[]',
		filterPlaceholder: 'Pesquise um instrumento',
		selectAllText: 'Selecionar Todos!',
		nonSelectedText: 'Selecione uma opição',
		allSelectedText: 'Todos Selecionados',
		buttonWidth: '300px',
		maxHeight: 200
	});
	
	$('#goodSty').multiselect({
		includeSelectAllOption: true,
		enableCaseInsensitiveFiltering: true,
		checkboxName: 'goodSty[]',
		filterPlaceholder: 'Pesquise um estilo',
		selectAllText: 'Selecionar Todos!',
		nonSelectedText: 'Selecione uma opição',
		allSelectedText: 'Todos Selecionados',
		buttonWidth: '300px',
		maxHeight: 200
	});
	
	$('#badSty').multiselect({
		includeSelectAllOption: true,
		enableCaseInsensitiveFiltering: true,
		deselectAll: 'justVisible',
		checkboxName: 'badSty[]',
		filterPlaceholder: 'Pesquise um estilo',
		selectAllText: 'Selecionar Todos!',
		nonSelectedText: 'Selecione uma opição',
		allSelectedText: 'Todos Selecionados',
		buttonWidth: '300px',
		maxHeight: 200
	});	
});